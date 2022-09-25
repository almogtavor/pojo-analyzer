package io.github.almogtavor.processor;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.CompilationSubject;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;

class DetailedPojoAnnotationProcessorTest {
    @Language("JAVA")
    private final String sourceJavaFileOfTargetFileWithFields = "package io.github.almogtavor.mocks;\n" +
                                                                "                \n" +
                                                                "import io.github.almogtavor.annotations.DetailedPojo;\n" +
                                                                "                \n" +
                                                                "import java.util.Date;\n" +
                                                                "                \n" +
                                                                "@DetailedPojo\n" +
                                                                "public class TargetFile {\n" +
                                                                "    private String entityId;\n" +
                                                                "    private Date createdDate;\n" +
                                                                "    private String text;\n" +
                                                                "    private String html;\n" +
                                                                "                \n" +
                                                                "    public TargetFile(String entityId, Date createdDate, String text) {\n" +
                                                                "        this.entityId = entityId;\n" +
                                                                "        this.createdDate = createdDate;\n" +
                                                                "        this.text = text;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public String getEntityId() {\n" +
                                                                "        return entityId;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setEntityId(String entityId) {\n" +
                                                                "        this.entityId = entityId;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public Date getCreatedDate() {\n" +
                                                                "        return createdDate;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setCreatedDate(Date createdDate) {\n" +
                                                                "        this.createdDate = createdDate;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public String getText() {\n" +
                                                                "        return text;\n" +
                                                                "    }\n" +
                                                                "                \n" +
                                                                "    public void setText(String text) {\n" +
                                                                "        this.text = text;\n" +
                                                                "    }\n" +
                                                                "}";
    @Language("JAVA")
    private final String sourceJavaFileOfTargetFileWithoutFields = "package io.github.almogtavor.mocks;\n" +
                                                                   "                \n" +
                                                                   "import io.github.almogtavor.annotations.DetailedPojo;\n" +
                                                                   "                                                \n" +
                                                                   "@DetailedPojo\n" +
                                                                   "public class TargetFile {\n" +
                                                                   "}";

    @Test
    void testSuccessfulCodeGenerator() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new DetailedPojoAnnotationProcessor())
                .compile(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.TargetFile", sourceJavaFileOfTargetFileWithFields));
        CompilationSubject.assertThat(compilation).succeeded();
    }

    @Test
    void shouldGenerateExpectedFileWhenGivenFileWithFields() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new DetailedPojoAnnotationProcessor())
                .compile(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.TargetFile", sourceJavaFileOfTargetFileWithFields));
        @Language("JAVA")
        String expected = "package io.github.almogtavor.mocks;\n" +
                          "                        \n" +
                          "import io.github.almogtavor.model.FieldDetails;\n" +
                          "import java.lang.String;\n" +
                          "import java.util.Arrays;\n" +
                          "import java.util.Date;\n" +
                          "import java.util.List;\n" +
                          "import javax.annotation.Generated;\n" +
                          "               \n" +
                          "@Generated(\"io.github.almogtavor.processor.DetailedPojoAnnotationProcessor\")\n" +
                          "public class DetailedTargetFile {\n" +
                          "    public static final List<FieldDetails> fieldDetailsList = Arrays.asList(\n" +
                          "            new FieldDetails<TargetFile, String>(\"entityId\", (TargetFile t) -> t.getEntityId(), (TargetFile t1, String t2) -> t1.setEntityId(t2)),\n" +
                          "            new FieldDetails<TargetFile, Date>(\"createdDate\", (TargetFile t) -> t.getCreatedDate(), (TargetFile t1, Date t2) -> t1.setCreatedDate(t2)),\n" +
                          "            new FieldDetails<TargetFile, String>(\"text\", (TargetFile t) -> t.getText(), (TargetFile t1, String t2) -> t1.setText(t2)));\n" +
                          "}";
        CompilationSubject.assertThat(compilation)
                .generatedSourceFile("io.github.almogtavor.mocks.DetailedTargetFile")
                .hasSourceEquivalentTo(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.DetailedTargetFile", expected));
    }

    @Test
    void shouldGenerateExpectedFileWhenGivenFileWithoutFields() {
        Compilation compilation = Compiler.javac()
                .withProcessors(new DetailedPojoAnnotationProcessor())
                .compile(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.TargetFile", sourceJavaFileOfTargetFileWithoutFields));
        @Language("JAVA")
        String expected = "package io.github.almogtavor.mocks;\n" +
                          "                    \n" +
                          "import io.github.almogtavor.model.FieldDetails;\n" +
                          "import java.util.Arrays;\n" +
                          "import java.util.List;\n" +
                          "import javax.annotation.Generated;\n" +
                          "                    \n" +
                          "@Generated(\"io.github.almogtavor.processor.DetailedPojoAnnotationProcessor\")\n" +
                          "public class DetailedTargetFile {\n" +
                          "    public static final List<FieldDetails> fieldDetailsList = Arrays.asList();\n" +
                          "}";
        CompilationSubject.assertThat(compilation)
                .generatedSourceFile("io.github.almogtavor.mocks.DetailedTargetFile")
                .hasSourceEquivalentTo(JavaFileObjects.forSourceString("io.github.almogtavor.mocks.DetailedTargetFile", expected));
    }

}