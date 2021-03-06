package roqme.generator.project;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import org.ecore.base.basicAttributes.EnumerationElement;
import org.ecore.base.basicAttributes.InlineEnumerationType;
import org.ecore.base.basicAttributes.PRIMITIVE_TYPE_NAME;
import org.ecore.base.basicAttributes.PrimitiveType;
import org.ecore.service.communicationObject.Enumeration;

@SuppressWarnings("all")
public class RoqmeProjectInitializer {
  public void generateModels(final String projectName, final List<Map<String, Object>> contexts, final IFileSystemAccess2 fsa) {
    fsa.generateFile((projectName + ".roqme"), this.createRoqmeModel(projectName, contexts));
    fsa.generateFile((projectName + ".roqmemap"), this.createRoqmemapModel(projectName, contexts));
  }
  
  public CharSequence createRoqmeModel(final String projectName, final List<Map<String, Object>> contexts) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("* RoQME Model. Generated by the RoQME Modeling Tool");
    _builder.newLine();
    _builder.append(" ");
    _builder.append("*/");
    _builder.newLine();
    _builder.append(" ");
    _builder.newLine();
    _builder.append("roqme ");
    _builder.append(projectName);
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    {
      for(final Map<String, Object> ctx : contexts) {
        _builder.append("context ");
        Object _get = ctx.get("context");
        _builder.append(_get);
        _builder.append(" : ");
        {
          Object _get_1 = ctx.get("attrType");
          if ((_get_1 instanceof PrimitiveType)) {
            Object _get_2 = ctx.get("attrType");
            final PrimitiveType value = ((PrimitiveType) _get_2);
            {
              boolean _equalsIgnoreCase = value.getTypeName().toString().equalsIgnoreCase("Boolean");
              if (_equalsIgnoreCase) {
                _builder.append("boolean");
              } else {
                boolean _equalsIgnoreCase_1 = value.getTypeName().toString().equalsIgnoreCase("String");
                if (_equalsIgnoreCase_1) {
                  _builder.append("TYPE NOT SUPPORTED");
                } else {
                  _builder.append("number");
                }
              }
            }
          } else {
            Object _get_3 = ctx.get("attrType");
            if ((_get_3 instanceof InlineEnumerationType)) {
              Object _get_4 = ctx.get("attrType");
              final InlineEnumerationType value_1 = ((InlineEnumerationType) _get_4);
              _builder.append("enum ");
              {
                EList<EnumerationElement> _enums = value_1.getEnums();
                boolean _hasElements = false;
                for(final EnumerationElement e : _enums) {
                  if (!_hasElements) {
                    _hasElements = true;
                    _builder.append("{");
                  } else {
                    _builder.appendImmediate(", ", "");
                  }
                  String _name = e.getName();
                  _builder.append(_name);
                }
                if (_hasElements) {
                  _builder.append("}");
                }
              }
            } else {
              Object _get_5 = ctx.get("attrType");
              if ((_get_5 instanceof Enumeration)) {
                Object _get_6 = ctx.get("attrType");
                final Enumeration value_2 = ((Enumeration) _get_6);
                _builder.append("enum ");
                {
                  EList<EnumerationElement> _enums_1 = value_2.getEnums();
                  boolean _hasElements_1 = false;
                  for(final EnumerationElement e_1 : _enums_1) {
                    if (!_hasElements_1) {
                      _hasElements_1 = true;
                      _builder.append("{");
                    } else {
                      _builder.appendImmediate(", ", "");
                    }
                    String _name_1 = e_1.getName();
                    _builder.append(_name_1);
                  }
                  if (_hasElements_1) {
                    _builder.append("}");
                  }
                }
              }
            }
          }
        }
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public CharSequence createRoqmemapModel(final String projectName, final List<Map<String, Object>> contexts) {
    CharSequence _xblockexpression = null;
    {
      ArrayList<String> imports = new ArrayList<String>();
      Resource res = null;
      for (final Map<String, Object> ctx : contexts) {
        {
          Object _get = ctx.get("resource");
          res = ((Resource) _get);
          boolean _contains = imports.contains(res.getURI().toString());
          boolean _not = (!_contains);
          if (_not) {
            imports.add(res.getURI().toString());
          }
        }
      }
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
      _builder.append("import \"");
      _builder.append(projectName);
      _builder.append(".roqme\"");
      _builder.newLineIfNotEmpty();
      {
        for(final String i : imports) {
          _builder.append("import \"");
          _builder.append(i);
          _builder.append("\"");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.newLine();
      {
        for(final Map<String, Object> ctx_1 : contexts) {
          _builder.append("context ");
          Object _get = ctx_1.get("context");
          _builder.append(_get);
          _builder.append(" monitor {");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("service : ");
          Object _get_1 = ctx_1.get("repository");
          _builder.append(_get_1, "\t");
          _builder.append(".");
          Object _get_2 = ctx_1.get("service");
          _builder.append(_get_2, "\t");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("attribute : ");
          Object _get_3 = ctx_1.get("attribute");
          _builder.append(_get_3, "\t");
          {
            Object _get_4 = ctx_1.get("attrType");
            if ((_get_4 instanceof PrimitiveType)) {
              Object _get_5 = ctx_1.get("attrType");
              final PrimitiveType value = ((PrimitiveType) _get_5);
              _builder.append(" [");
              PRIMITIVE_TYPE_NAME _typeName = value.getTypeName();
              _builder.append(_typeName, "\t");
              _builder.append("]");
            } else {
              Object _get_6 = ctx_1.get("attrType");
              if ((_get_6 instanceof InlineEnumerationType)) {
                _builder.append(" [Enum]");
              } else {
                Object _get_7 = ctx_1.get("attrType");
                if ((_get_7 instanceof Enumeration)) {
                  _builder.append(" [Enum]");
                }
              }
            }
          }
          _builder.newLineIfNotEmpty();
          _builder.append("}");
          _builder.newLine();
        }
      }
      _xblockexpression = _builder;
    }
    return _xblockexpression;
  }
}
