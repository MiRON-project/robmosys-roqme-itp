package roqme.generator.project;

import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess2;

@SuppressWarnings("all")
public class RoqmeEmptyProjectInitializer {
  public void generateModels(final String projectName, final IFileSystemAccess2 fsa) {
    fsa.generateFile((projectName + ".roqme"), this.createRoqmeModel(projectName));
    fsa.generateFile((projectName + ".roqmemap"), this.createRoqmemapModel(projectName));
  }
  
  public CharSequence createRoqmeModel(final String projectName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* RoQME Model. Generated by the RoQME Modeling Tool");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* RoQME allows you to specify Context variables (e.g., battery level) and, ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* from them, relevant context patterns (e.g., “the battery level drops more ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* than 1% per minute”). The detection of a context pattern is considered an ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Observation in a belief network, used to specify the dynamics of non-functional");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* properties (e.g., power consumption). The degree of fulfillment of these ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* non-functional properties are expressed as QoS Metrics.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append("  ");
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Syntax example:");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* roqme example");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* property powerConsumption");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* context batteryLevel : number");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* observation obs1 : batteryLevel < 20 undermines powerConsumption");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/\t");
    _builder.newLine();
    return _builder;
  }
  
  public CharSequence createRoqmemapModel(final String projectName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* RoQME-to-RobMoSys mapping model. Generated by the RoQME Modeling Tool");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* This mapping model gathers a set of monitors, each one linked to a");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* RoQME Context and to a RobMoSys ServiceDefinition, describing the ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* associated context provider.");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import \"");
    _builder.append(projectName);
    _builder.append(".roqme\"");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* Syntax example:");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* import \"CommBasicObjects.types\"");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* import \"CommBasicObjects.services\"");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* context batteryLevel monitor {");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*   service : CommBasicObjects.BatteryQueryService ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*   attribute : CommBasicObjects.CommBatteryData.level [Integer]");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*   period : 10s ");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* }");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    return _builder;
  }
}
