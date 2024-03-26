package us.abstracta.jmeter.javadsl.core.configs;

import com.sap.csc.ems.config.EMSConfig;
import org.apache.jmeter.testbeans.gui.TestBeanGUI;
import org.apache.jmeter.testelement.TestElement;
import us.abstracta.jmeter.javadsl.codegeneration.MethodCall;
import us.abstracta.jmeter.javadsl.codegeneration.MethodCallBuilder;
import us.abstracta.jmeter.javadsl.codegeneration.MethodCallContext;

import java.lang.reflect.Method;
import java.util.List;

public class DslEMSConfig extends BaseConfigElement {
   public DslEMSConfig() {
        super("EMSConfig", TestBeanGUI.class);
   }

   protected TestElement buildTestElement() {
      return new EMSConfig();
   }

   public static class CodeBuilder extends MethodCallBuilder {
      public CodeBuilder(List<Method> builderMethods) {
         super(builderMethods);
      }

       @Override
       public boolean matches(MethodCallContext context) {
           TestElement testElement = context.getTestElement();
           return testElement.getClass() == EMSConfig.class;
       }

       @Override
       protected MethodCall buildMethodCall(MethodCallContext context) {
           TestElement testElement = context.getTestElement();
           return buildMethodCall();
       }
   }
}
