package us.abstracta.jmeter.javadsl.core.controllers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import us.abstracta.jmeter.javadsl.core.util.StringTemplate;

public class DslTestFragmentControllerTest extends MethodCallFragmentBuilderTest {

  public static final String DEFAULT_FRAGMENT_NAME = "Test Fragment";

  public static String buildFragmentJmx(String name, String... childrenJmx) {
    return buildFragmentJmx(name, true, childrenJmx);
  }

  private static String buildFragmentJmx(String name, boolean enabled, String... childrenJmx) {
    return new StringTemplate(testResourceContents("fragments/fragment.template.jmx"))
        .bind("name", name)
        .bind("enabled", enabled)
        .bind("children", String.join("\n", childrenJmx))
        .solve();
  }

  public static String buildFragmentDisabledJmx(String... childrenJmx) {
    return buildFragmentJmx(DEFAULT_FRAGMENT_NAME, false, childrenJmx);
  }

  public static String buildFragmentMethod(String methodName, String fragmentName,
      String... children) {
    return new StringTemplate(testResourceContents("fragments/TestFragmentMethodDsl.template.java"))
        .bind("methodName", methodName)
        .bind("fragmentName", DEFAULT_FRAGMENT_NAME.equals(fragmentName) ? ""
            : String.format("\"%s\",", fragmentName))
        .bind("children", String.join("\n,", children))
        .solve();
  }

  @Nested
  public class CodeBuilderTest {

    @Test
    public void shouldGenerateDslWithFragmentMethodWhenConvertTestPlanWithFragment(
        @TempDir Path tmp) throws IOException {
      String testPlanJmx = buildTestPlanJmx(
          buildFragmentJmx(DEFAULT_FRAGMENT_NAME, buildHttpSamplerJmx()));
      String methodName = "testFragment";
      assertThat(jmx2dsl(testPlanJmx, tmp))
          .isEqualTo(buildTestPlanDsl(
              buildFragmentMethod(methodName, DEFAULT_FRAGMENT_NAME, buildHttpSamplerDsl()),
              methodName + "()"));
    }

    private String buildTestPlanDsl(String method, String child) {
      return buildTestPlanDsl(Collections.singletonList(method), Collections.singletonList(child));
    }

    public String buildTestPlanDsl(List<String> methods, List<String> children) {
      return buildTestPlanDslTemplate(children)
          .staticImports(Collections.singleton(DslTestFragmentController.class.getName()))
          .imports(Collections.singleton(DslTestFragmentController.class.getName()))
          .methodDefinitions(methods)
          .solve();
    }

    @Test
    public void shouldGenerateDslWithFragmentNameWhenConvertFragmentNonDefaultName(
        @TempDir Path tmp) throws IOException {
      String fragmentName = "My Fragment";
      String testPlanJmx = buildTestPlanJmx(
          buildFragmentJmx(fragmentName, buildHttpSamplerJmx()));
      String methodName = "myFragment";
      assertThat(jmx2dsl(testPlanJmx, tmp))
          .isEqualTo(buildTestPlanDsl(
              buildFragmentMethod(methodName, fragmentName, buildHttpSamplerDsl()),
              methodName + "()"));
    }

    @Test
    public void shouldGenerateDslWithFragmentNameWhenConvertFragmentWithNameStartingWithDigit(
        @TempDir Path tmp) throws IOException {
      String fragmentName = "2Fragment";
      String testPlanJmx = buildTestPlanJmx(
          buildFragmentJmx(fragmentName, buildHttpSamplerJmx()));
      String methodName = "fragment" + fragmentName;
      assertThat(jmx2dsl(testPlanJmx, tmp))
          .isEqualTo(buildTestPlanDsl(
              buildFragmentMethod(methodName, fragmentName, buildHttpSamplerDsl()),
              methodName + "()"));
    }

    @Test
    public void shouldGenerateDslWithFragmentNameWhenConvertFragmentWithNameWithSpecialChars(
        @TempDir Path tmp) throws IOException {
      String fragmentName = "My(){Fragment}";
      String testPlanJmx = buildTestPlanJmx(
          buildFragmentJmx(fragmentName, buildHttpSamplerJmx()));
      String methodName = "myFragment";
      assertThat(jmx2dsl(testPlanJmx, tmp))
          .isEqualTo(buildTestPlanDsl(
              buildFragmentMethod(methodName, fragmentName, buildHttpSamplerDsl()),
              methodName + "()"));
    }

    @Test
    public void shouldGenerateDslWithFragmentsWhenConvertFragmentsWithCollidingNames(
        @TempDir Path tmp) throws IOException {
      String testPlanJmx = buildTestPlanJmx(
          buildFragmentJmx(DEFAULT_FRAGMENT_NAME, buildHttpSamplerJmx()),
          buildFragmentJmx(DEFAULT_FRAGMENT_NAME, buildHttpSamplerJmx()));
      String methodName1 = "testFragment";
      String methodName2 = "testFragment2";
      assertThat(jmx2dsl(testPlanJmx, tmp))
          .isEqualTo(buildTestPlanDsl(
              Arrays.asList(
                  buildFragmentMethod(methodName1, DEFAULT_FRAGMENT_NAME, buildHttpSamplerDsl()),
                  buildFragmentMethod(methodName2, DEFAULT_FRAGMENT_NAME, buildHttpSamplerDsl())),
              Arrays.asList(methodName1 + "()", methodName2 + "()")
          ));
    }

    @Test
    public void shouldGenerateDslWithCommentedFragmentCallWhenConvertDisabledFragment(
        @TempDir Path tmp) throws IOException {
      String testPlanJmx = buildTestPlanJmx(
          buildFragmentDisabledJmx(DEFAULT_FRAGMENT_NAME, buildHttpSamplerJmx()));
      String methodName = "testFragment";
      assertThat(jmx2dsl(testPlanJmx, tmp))
          .isEqualTo(buildTestPlanDsl(
              buildFragmentMethod(methodName, DEFAULT_FRAGMENT_NAME, buildHttpSamplerDsl()),
              "//" + methodName + "()"));
    }

  }

}
