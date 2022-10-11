# Pojo Analyzer

This library allows generation of a list that will contain getter, setter, and string name for each field of a POJO.
This can be necessary for various use cases like iterating all fields of a POJO, manipulating fields of a POJO based on external properties and more.
This library is different from other approaches to this problem in that it does the list generation at compile time, hence there is no performance issue.

### Install
```kotlin
dependencies {
  implementation("io.github.almogtavor:pojo-analyzers:1.0.0")
}
```

### How does it work? Using `@DetailedPojo`

```java
import io.github.almogtavor.annotations.DetailedPojo;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@DetailedPojo
@AllArgsConstructor
@Data
public class TargetPojo {
    private String entityId;
    private Date createdDate;
    private String text;
}
```

Causes Pojo Analyzer to generate:

```java
import io.github.almogtavor.model.FieldDetails;
import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Generated("io.github.almogtavor.processor.DetailedPojoAnnotationProcessor")
public class DetailedTargetPojo {
    public static final List<FieldDetails> fieldDetailsList = Arrays.asList(
            new FieldDetails<TargetFile, String>("entityId", (TargetFile t) -> t.getEntityId(), 
                    (TargetFile t1, String t2) -> t1.seEntityId(t2)),
            new FieldDetails<TargetFile, Date>("createdDate", (TargetFile t) -> t.getCreatedDate(), 
                    (TargetFile t1, Date t2) -> t1.setCreatedDate(t2)),
            new FieldDetails<TargetFile, String>("text", (TargetFile t) -> t.getText(), 
                    (TargetFile t1, String t2) -> t1.setText(t2)));
}
```
And now we can easily access the getter, setter and name of each field, since the `FieldDetails` class looks like this:
```java
import lombok.Data;

@Data
public class FieldDetails<ClassTypeT, FieldTypeT> {
    private String fieldName;
    private Function<ClassTypeT, FieldTypeT> fieldGetter;
    private BiConsumer<ClassTypeT, FieldTypeT> fieldSetter;
}
```

### Requirements
The project has been compiled with JDK 1.8 for wider compatibility.

### Limitations
Currently, there is no support in primitive types which will cause a runtime exception.
