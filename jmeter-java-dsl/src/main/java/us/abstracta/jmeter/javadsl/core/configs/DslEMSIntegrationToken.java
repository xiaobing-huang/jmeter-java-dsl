package us.abstracta.jmeter.javadsl.core.configs;

import com.sap.csc.ems.integration.token.EMSIntegrationToken;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import us.abstracta.jmeter.javadsl.codegeneration.MethodCall;
import us.abstracta.jmeter.javadsl.codegeneration.MethodCallBuilder;
import us.abstracta.jmeter.javadsl.codegeneration.MethodCallContext;

import java.lang.reflect.Method;
import java.util.List;

public class DslEMSIntegrationToken extends BaseConfigElement {
  public DslEMSIntegrationToken() {
    super("EMSIntegrationToken", TestBeanGUI.class);
  }

  protected TestElement buildTestElement() {
    return new EMSIntegrationToken();
  }

  public static class CodeBuilder extends MethodCallBuilder {
    public CodeBuilder(List<Method> builderMethods) {
      super(builderMethods);
    }

    @Override
    public boolean matches(MethodCallContext context) {
      TestElement testElement = context.getTestElement();
      return testElement.getClass() == EMSIntegrationToken.class;
    }

    @Override
    protected MethodCall buildMethodCall(MethodCallContext context) {
      TestElement testElement = context.getTestElement();
      return buildMethodCall();
    }
  }
}
