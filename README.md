# RoQME: Tools and models for the assessment of Non-Functional properties in RobMoSys

Building systems out of components requires taking into account both functional and non-functional properties. Non-functional properties define “how” a system performs rather than “what” it does. Examples of non-functional properties include dependability, safety, security, resource consumption, customer satisfaction, etc.

RobMoSys takes non-functional properties into account from the very beginning, treating them as first-class citizens. RobMoSys considers the management of non-functional properties as key added-value services built on top of its Ecosystem. In this vein, RoQME wants to contribute to RobMoSys a model-based framework for dealing with system-level non-functional properties, enabling the specification of global robot Quality of Service (QoS) metrics defined in terms of the (internal and external) contextual information available.

The RoQME toolchain is intended to support the role of QoS Engineers, providing them with a specific QoS view. This view will allow them to model system-level robot QoS metrics according to the new RoQME meta-model. The concepts included in the RoQME meta-model will be linked to those already available in the RobMoSys meta-model (defined by other roles in different views). In order to guarantee the correct alignment and harmonization of the new role, view and meta-model with those of RobMoSys, a RoQME-to-RobMoSys mapping meta-model, linking both worlds, will also be defined. This mapping aims at promoting high-cohesion and loose coupling among the different RobMoSys views, providing a non-intrusive way of extending the RobMoSys meta-model, i.e., modifying the RoQME meta-model would only imply adapting the mapping to RobMoSys (but not modifying the RobMoSys meta-model itself), and vice versa.

The RoQME toolchain, delivered as an Eclipse plug-in, provides both modelling and code generation tools, enabling the creation of actual RobMoSys-compliant components, readily usable in RobMoSys-based solutions as QoS information providers. This information could then be used by other components for different purposes, e.g., robot behaviour adaptation or benchmarking.


