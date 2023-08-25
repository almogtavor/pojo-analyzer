# Pojo Analyzer

This library allows generation of a list that will contain a getter, setter, and string name for each field of a POJO.
This can be necessary for various use cases like iterating all fields of a POJO, manipulating fields of a POJO based on
external properties, and more. This library is different from other approaches to this problem in that it does the list
generation at compile time, hence there is no performance issue.

### Install
```kotlin
dependencies {
  compileOnly("io.github.almogtavor:pojo-analyzers:1.3.0")
  annotationProcessor("io.github.almogtavor:pojo-analyzers:1.3.0")
}
```

### Why
There is sometimes a need for accessing a field name in Java, as well as its value (e.g. getter) or its setter.
The most common need for this is to enable the usage of configuration properties that define actions based on POJOs' fields. 
These questions emphasize the general requirement ([question 1](https://stackoverflow.com/questions/14944333/get-name-of-a-field), [question 2](https://stackoverflow.com/questions/13400075/reflection-generic-get-field-value)).
Lombok does a great job providing `@FieldNameConstants`. But this is not enough, since `@FieldNameConstants` generates the name of the field, but not an acces to its getter nor setter. Therefore there is no way of interacting with the field after accessing its name.
Another requirement that gets solved by `pojo-analyzer` is the need for iteration of all POJO's fields. These questions emphasize this ([question 1](https://stackoverflow.com/questions/17095628/loop-over-all-fields-in-a-java-class), [question 2](https://stackoverflow.com/questions/3333974/how-to-loop-over-a-class-attributes-in-java)).

### How does it work? Using `@DetailedPojo`

```java
import io.github.almogtavor.pojo.analyzer.annotations.DetailedPojo;

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
import io.github.almogtavor.pojo.analyzer.model.FieldDetails;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Generated("io.github.almogtavor.pojo.analyzer.processor.DetailedPojoAnnotationProcessor")
public class DetailedTargetPojo {
    public static final Map<String, FieldDetails> map = new HashMap<String, FieldDetails>() {{
        put("entityId", new FieldDetails<TargetPojo, String>("entityId", (TargetPojo t) -> t.getEntityId(), (TargetPojo t1, String t2) -> t1.setEntityId(t2)));
        put("createdDate", new FieldDetails<TargetPojo, Date>("createdDate", (TargetPojo t) -> t.getCreatedDate(), (TargetPojo t1, Date t2) -> t1.setCreatedDate(t2)));
        put("text", new FieldDetails<TargetPojo, String>("text", (TargetPojo t) -> t.getText(), (TargetPojo t1, String t2) -> t1.setText(t2)));
    }};
}
```

So we can use it as follows:
```java
TargetPojo targetPojo = new TargetPojo("123", new Date(), "pojo-analyzer");
FieldDetails<TargetPojo, String> entityIdField = DetailedTargetPojo.map.get("entityId");
entityIdField.getFieldValue(targetPojo); // "123"
```

We can also specify:
```java
import io.github.almogtavor.pojo.analyzer.model.VariableType;
        
@DetailedPojo(variableType = VariableType.LIST)
public class TargetPojo {
    private String entityId;
    private Date createdDate;
    private String text;
}
```
So we will get a generated list:

```java
import io.github.almogtavor.pojo.analyzer.model.FieldDetails;

import javax.annotation.processing.Generated;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Generated("io.github.almogtavor.pojo.analyzer.processor.DetailedPojoAnnotationProcessor")
public class DetailedTargetPojo {
    public static final List<FieldDetails> list = Arrays.asList(
            new FieldDetails<TargetPojo, String>("entityId", (TargetPojo t) -> t.getEntityId(),
                    (TargetPojo t1, String t2) -> t1.seEntityId(t2)),
            new FieldDetails<TargetPojo, Date>("createdDate", (TargetPojo t) -> t.getCreatedDate(),
                    (TargetPojo t1, Date t2) -> t1.setCreatedDate(t2)),
            new FieldDetails<TargetPojo, String>("text", (TargetPojo t) -> t.getText(),
                    (TargetPojo t1, String t2) -> t1.setText(t2)));
}
```

Now we can easily access the getter, setter, and name of each field since the `FieldDetails` class looks like this:

```java
import lombok.Data;

@Data
public class FieldDetails<ClassTypeT, FieldTypeT> {
    private String fieldName;
    private Function<ClassTypeT, FieldTypeT> fieldGetter;
    private BiConsumer<ClassTypeT, FieldTypeT> fieldSetter;
}
```

We can also easily perform iteration over all fields of a class.
For example:
```java
class MyClass {
    void iterateClasses() {
        DetailedTargetPojo.list.stream().forEach(System.out::println);
    }
}
```

### Requirements

The project has been compiled with JDK 1.8 for wider compatibility.

### Limitations
Currently, there is no support for primitive types which will cause a runtime exception.
