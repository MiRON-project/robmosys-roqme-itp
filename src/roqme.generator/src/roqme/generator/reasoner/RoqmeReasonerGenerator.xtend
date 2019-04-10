//  This file belongs to the RoQME toolchain.
//  Copyright (C) 2019  University of Extremadura, University of Málaga, Biometric Vox.
//
//  RoQME is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 3 of the License, or
//  any later version.
//
//  RoQME is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  GNU GPLv3 link: http://www.gnu.org/licenses/gpl-3.0.html

package roqme.generator.reasoner

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.AbstractGenerator
import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.xtext.generator.IGeneratorContext
import roqme.metamodel.datatypes.RoqmeModel
import roqme.metamodel.kernel.BeliefVariable
import roqme.metamodel.kernel.Observation
import java.util.ArrayList
import roqme.metamodel.kernel.SetEvidence
import java.util.List
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import roqme.metamodel.datatypes.TimeValue
import roqme.metamodel.kernel.InfluenceEnum

class RoqmeReasonerGenerator extends AbstractGenerator {

	final int DEFAULT_PROP_SURVIVAL_SEC = 300
	final double DEFAULT_PROP_REFERENCE = 0.5

	private ArrayList<String> variableNames = new ArrayList<String>();
	private ArrayList<String> propertyNames = new ArrayList<String>();


	override void doGenerate(Resource resource, IFileSystemAccess2 fsa, IGeneratorContext context) {

		if(resource.getURI().fileExtension().equalsIgnoreCase("roqme")) {
			for (model : resource.allContents.toIterable.filter(RoqmeModel)) {		
					
				if(model.namespace.isNullOrEmpty){
					val splitStr = resource.getURI().lastSegment().split('\\.')
					model.namespace = splitStr.get(0)
				}
			
				fsa.generateFile(model.namespace + ".duai", generateDUAI(model))
				fsa.generateFile(model.namespace + ".config", generateConfig())
				fsa.generateFile("run_" + model.namespace + ".sh", generateScript(model.namespace))
			} 
		}
	}
	
	def generateScript(String fileName) {
		'''
		#!/bin/bash
		# Starts the RoQME reasoner. Generated by the RoQME Modeling Tool
		
		./roqme-reasoner «fileName».duai «fileName».config
		
		'''
	}
	
	def generateConfig() {
		'''
		# Configuration of the RoQME reasoner. Generated by the RoQME Modeling Tool
		
		period.ms = 4000
		inference.method = Filtering-VE
		variables.names = «
		FOR variable : variableNames SEPARATOR ", "»«
			variable»«
		ENDFOR»
		variables.query = «
		FOR prop : propertyNames SEPARATOR ", "»«
			prop»«
		ENDFOR»
		'''
	}
	
	def generateDUAI(RoqmeModel model) {
		
		var index = 0;
		var two_tbn = new StringBuffer()
		var sensor  = new StringBuffer()
		var domains = new StringBuffer()
		var factors = new StringBuffer()

		var List<Double> prior = new ArrayList<Double>()
		var List<Double> transition = new ArrayList<Double>()
		var List<Double> obsProb = new ArrayList<Double>()

		// Loop over belief variables
		for(beliefVar : model.variables.filter(BeliefVariable)) {
			propertyNames.add(beliefVar.getName())
			variableNames.add(beliefVar.getName())
			variableNames.add(beliefVar.getName() + "_p")
			
			calculatePropProb(
				beliefVar.getReference(), 
				getTimeInSec(beliefVar.getSurvival()), 
				prior, transition)
				
			factors.append(probToString(prior))
			factors.append(probToString(transition))
			
			two_tbn.append(index)
			two_tbn.append(" ")
			domains.append("1")
			domains.append(" ")
			domains.append(index)
			domains.append("\n")
			index++
			
			two_tbn.append(index)
			two_tbn.append(" ")
			domains.append("2")
			domains.append(" ")
			domains.append(index)
			domains.append(" ")
			domains.append(index-1)
			domains.append("\n")
			index++
		}
		
		var Iterable<SetEvidence> evList
		val nBeliefVar = variableNames.size()
		
		// Loop over observations
		for(obs : model.sentences.filter(Observation)) {
			variableNames.add(obs.getName())
			sensor.append(index)
			sensor.append(" ")
			
			evList = obs.actions.filter(SetEvidence)
			domains.append(evList.size()+1)
			domains.append(" ")
			domains.append(index)

			for(ev : evList) {
				domains.append(" ")
				domains.append(variableNames.indexOf(ev.getTarget().getName()))
				
				obsProb.clear()
				calculateObsProb(
					ev.getInfluence() == InfluenceEnum::REINFORCE, 
					ev.getStrength().getValue(), obsProb)
					
				factors.append(probToString(obsProb))
			}
			domains.append("\n")
			index++
		}

		index = 0
		'''
		# Generated by the RoQME Modeling Tool
		
		DBAYES
		
		# Variables
		«variableNames.size()»
		«FOR aux : variableNames»
			2	# id=«index» name=«variableNames.get(index++)» domain={0,1}
		«ENDFOR»
		
		# 2TBN
		«nBeliefVar» «two_tbn.toString()»
		
		# Sensors
		«variableNames.size()-nBeliefVar» «sensor.toString()»
		
		# Domains
		«domains.toString()»
		
		# Factors
		«factors.toString()»
		
		'''
	}

	def calculatePropProb(double reference, double survival_sec, List<Double> prior, List<Double> transition) {

		val refVal = if (reference >= 0 && reference < 1) reference 
						else if (reference == 1) 0.99 
						else  DEFAULT_PROP_REFERENCE
		val survivalVal = if(survival_sec > 0) survival_sec else DEFAULT_PROP_SURVIVAL_SEC
		var EigenDecomposition eigen
		
		try {
			val double lambda = 1/survivalVal
			val double mu = lambda * refVal / (1 - refVal)

			eigen = new EigenDecomposition(
				MatrixUtils.createRealMatrix(#[#[-lambda, lambda], #[mu, -mu]])
			)
		}
		catch(Exception e) {
			val double lambda = 1/DEFAULT_PROP_SURVIVAL_SEC
			val double mu = lambda * DEFAULT_PROP_REFERENCE / (1 - DEFAULT_PROP_REFERENCE)
			
			eigen = new EigenDecomposition(
				MatrixUtils.createRealMatrix(#[#[-lambda, lambda], #[mu, -mu]])
			)
		}
		
		prior.clear();
		transition.clear();
		
		// P(¬h)
		prior.add(1- refVal)
		
		// P(¬h)
		prior.add(refVal)
		
		// Calculating transition probabilities
		var double[][] d = eigen.getD().getData()
		for(var i=0; i<d.length; i++)
			d.get(i).set(i, Math.exp(d.get(i).get(i)))

		val RealMatrix matV_inv = MatrixUtils.inverse(eigen.getV())
		val RealMatrix matP = eigen.getV().multiply(MatrixUtils.createRealMatrix(d)).multiply(matV_inv)
		
		transition.add(matP.getEntry(1,1)) 	// P(¬h(t+1) | ¬h(t))
		transition.add(matP.getEntry(0,1))	// P(¬h(t+1) | h(t))
		transition.add(matP.getEntry(1,0))	// P(h(t+1) | ¬h(t))
		transition.add(matP.getEntry(0,0))	// P(h(t+1) | h(t))
	}
	
	def calculateObsProb(boolean reinforces, int strength, List<Double> probability) {
		
		val lr = if(strength == 0) 0.1 	// VERY HIGH
			else if(strength == 1) 0.3 	// HIGH
			else if(strength == 2) 0.5 	// DEFAULT
			else if(strength == 3) 0.7	// LOW
			else if(strength == 4) 0.9	// VERY LOW
			else 0.5
			
		var aux = Math.random() * 0.95 + 0.025
			
		probability.clear();
			
		if(reinforces) {
			probability.add(1-lr*aux) 	// P(¬obs | ¬h)
			probability.add(1-aux) 		// P(¬obs | h)
			probability.add(lr*aux) 	// P(obs | ¬h)
			probability.add(aux) 		// P(obs | h)
		}
		else {
			probability.add(lr*aux) 	// P(¬obs | ¬h)
			probability.add(aux) 		// P(¬obs | h)
			probability.add(1-lr*aux) 	// P(obs | ¬h)
			probability.add(1-aux) 		// P(obs | h)
		}		
	}
	
	def getTimeInSec(TimeValue time) {

		var double result = 0;

		if(time !== null) {
			switch time.getUnit() {
				case "milliseconds",
				case "millisecond":
					result = time.getValue() / 1000
				case "seconds",
				case "second":
					result = time.getValue()
				case "minutes",
				case "minute":
					result = time.getValue() * 60
				case "hours",
				case "hour":
					result = time.getValue() * 60 * 60
				case "days",
				case "day":
					result = time.getValue() * 60 * 60 * 24
			}
		}
		return result;
	}
	
	def String probToString(List<Double> prob) {
		var StringBuffer result = new StringBuffer()
		result.append(prob.size())
		result.append(" ")
		
		for(p : prob) {
			result.append(p)
			result.append(" ")
		}
		return result.append("\n").toString()
	}
}