# Pojo Analyzer

Pojo Analyzer is a Java library designed to generate a list or a map containing getters, setters, and string names for each field of a Plain Old Java Object (POJO). 
It performs this generation at compile time, providing a performance-optimized solution for various use-cases like iterating over all fields of a POJO, and manipulating those fields based on external properties.

## Table of Contents
- [Installation](#installation)
- [Why Use Pojo Analyzer?](#why-use-pojo-analyzer)
- [How does it work?](#how-does-it-work-using-detailedpojo)
- [Requirements](#requirements)
- [Limitations](#limitations)

---

## Installation
```kotlin
dependencies {
  compileOnly("io.github.almogtavor:pojo-analyzers:1.3.0")
  annotationProcessor("io.github.almogtavor:pojo-analyzers:1.3.0")
}
```

---

## Why Use Pojo Analyzer?

- **Field Name Access:** Accessing a field's name as well as its getter and setter is sometimes necessary, especially for configuration properties that define actions based on POJOs' fields.
  
- **Iteration Over Fields:** The library simplifies the process of iterating over all fields in a POJO, which is a frequent requirement.
  
- **Performance:** Pojo Analyzer operates at compile-time, avoiding any runtime performance costs, which is usually how this problem gets solved.

These questions emphasize the general requirement ([question 1](https://stackoverflow.com/questions/14944333/get-name-of-a-field), [question 2](https://stackoverflow.com/questions/13400075/reflection-generic-get-field-value)).
Lombok does a great job providing `@FieldNameConstants`. But this is not enough, since `@FieldNameConstants` generates the name of the field, but not an acces to its getter nor setter. Therefore there is no way of interacting with the field after accessing its name.
Another requirement that gets solved by `pojo-analyzer` is the need for iteration of all POJO's fields. These questions emphasize this ([question 1](https://stackoverflow.com/questions/17095628/loop-over-all-fields-in-a-java-class), [question 2](https://stackoverflow.com/questions/3333974/how-to-loop-over-a-class-attributes-in-java)).

---

## How does it work? Using `@DetailedPojo`

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

## Requirements

The project has been compiled with JDK 1.8 for wider compatibility.

---

## Limitations
Currently, there is no support for primitive types which will cause a runtime exception.
