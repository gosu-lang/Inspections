package inspections;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.codeInspection.ex.BaseLocalInspectionTool;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

/**
 */
public class FooInspection extends BaseLocalInspectionTool {
  @Nls
  @NotNull
  @Override
  public String getGroupDisplayName() {
    return SampleBundle.message( "inspection.sample.name" );
  }

  @Nls
  @NotNull
  @Override
  public String getDisplayName() {
    return SampleBundle.message( "inspection.sample.foo.name" );
  }

  @NotNull
  @Override
  public String getShortName() {
    return "Foo";
  }

  @NotNull
  @Override
  public PsiElementVisitor buildVisitor( @NotNull final ProblemsHolder holder, boolean isOnTheFly ) {
    return new GosuElementVisitor() {
      @Override
      public void visitElement( PsiElement elem ) {
        if( isProblematic( elem ) ) {
          holder.registerProblem( elem, SampleBundle.message( "inspection.sample.foo.error" ), ProblemHighlightType.GENERIC_ERROR_OR_WARNING, new Fix( elem ) );
          return;
        }
        super.visitElement( elem );
      }
    };
  }

  private static boolean isProblematic( PsiElement elem ) {
    if( elem.getClass().getName().endsWith( "GosuIdentifierImpl" ) ) {
      return elem.getText().startsWith("Foo");
    }
    return false;
  }

  class Fix implements LocalQuickFix {
    private final FooQuickFix _fix;

    public Fix( PsiElement elem ) {
      _fix = new FooQuickFix( elem );
    }

    @NotNull
    public String getName() {
      return _fix.getText();
    }

    @NotNull
    public String getFamilyName() {
      return "Foo fix";
    }

    public void applyFix( @NotNull Project project, @NotNull ProblemDescriptor descriptor ) {
      PsiElement element = descriptor.getPsiElement();
      if( element == null ) {
        return;
      }
      final PsiFile psiFile = element.getContainingFile();
      if( _fix.isAvailable( project, null, psiFile ) ) {
        _fix.invoke( project, null, psiFile );
      }
    }
  }

}
