package com.bbuhot.errorprone;

import com.google.auto.service.AutoService;
import com.google.errorprone.BugPattern;
import com.google.errorprone.BugPattern.Category;
import com.google.errorprone.BugPattern.LinkType;
import com.google.errorprone.BugPattern.SeverityLevel;
import com.google.errorprone.VisitorState;
import com.google.errorprone.bugpatterns.BugChecker;
import com.google.errorprone.bugpatterns.BugChecker.MethodInvocationTreeMatcher;
import com.google.errorprone.bugpatterns.BugChecker.NewClassTreeMatcher;
import com.google.errorprone.matchers.Description;
import com.google.errorprone.util.ASTHelpers;
import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.NewClassTree;
import com.sun.source.tree.Tree;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.code.Symbol.MethodSymbol;
import java.util.Optional;
import javax.annotation.Nullable;

@AutoService(BugChecker.class)
@BugPattern(
    name = "TestOnlyCheck",
    category = Category.JDK,
    summary = "Test only methods are only allowed to be used in tests.",
    severity = SeverityLevel.ERROR,
    linkType = LinkType.NONE)
public class TestOnlyCheck extends BugChecker
    implements MethodInvocationTreeMatcher, NewClassTreeMatcher {

  @Override
  public Description matchMethodInvocation(MethodInvocationTree tree, VisitorState state) {
    TestOnly annotation = ASTHelpers.getAnnotation(tree, TestOnly.class);
    if (annotation != null) {
      return checkRestriction(annotation, tree, state);
    }

    MethodSymbol methSymbol = ASTHelpers.getSymbol(tree);

    Optional<MethodSymbol> superWithTestOnly =
        ASTHelpers.findSuperMethods(methSymbol, state.getTypes())
            .stream()
            .filter((t) -> ASTHelpers.hasAnnotation(t, TestOnly.class, state))
            .findFirst();

    if (!superWithTestOnly.isPresent()) {
      return Description.NO_MATCH;
    }
    return checkRestriction(
        ASTHelpers.getAnnotation(superWithTestOnly.get(), TestOnly.class), tree, state);
  }

  @Override
  public Description matchNewClass(NewClassTree tree, VisitorState state) {
    return checkRestriction(ASTHelpers.getAnnotation(tree, TestOnly.class), tree, state);
  }

  private Description checkRestriction(
      @Nullable TestOnly testOnly, Tree where, VisitorState state) {
    if (testOnly == null) {
      return Description.NO_MATCH;
    }
    if (state.errorProneOptions().isTestOnlyTarget()) {
      return Description.NO_MATCH;
    }

    ClassSymbol declaredClass = ASTHelpers.enclosingClass(ASTHelpers.getSymbol(where));

    ClassSymbol callingClass = null;
    for (Tree path : state.getPath()) {
      if (path instanceof ClassTree) {
        callingClass = ASTHelpers.getSymbol((ClassTree) path);
      }
    }

    if (declaredClass == callingClass) {
      return Description.NO_MATCH;
    }

    return buildDescription(where).build();
  }
}
