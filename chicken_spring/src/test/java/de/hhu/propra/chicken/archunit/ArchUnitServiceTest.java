package de.hhu.propra.chicken.archunit;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.fields;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.GeneralCodingRules;
import de.hhu.propra.chicken.ChickenApplication;


@AnalyzeClasses(packagesOf = ChickenApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchUnitServiceTest {

  @ArchTest
  ArchRule noDeprecatedClasses = ArchRuleDefinition.classes()
      .should()
      .notBeAnnotatedWith(Deprecated.class);

  @ArchTest
  ArchRule noDeprecatedMethods = ArchRuleDefinition.methods()
      .should()
      .notBeAnnotatedWith(Deprecated.class);

  @ArchTest
  ArchRule noFieldInjection = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

  @ArchTest
  ArchRule noFieldsAreNotPrivate = ArchRuleDefinition.fields()
      .that()
      .areDeclaredInClassesThat()
      .resideInAPackage("....")
      .should()
      .bePrivate();

}
