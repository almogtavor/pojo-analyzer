package io.github.almogtavor.mocks;

import io.github.almogtavor.pojo.analyzer.model.FieldDetails;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Generated("io.github.almogtavor.processor.DetailedPojoAnnotationProcessor")
public class DetailedTargetFile {
    public static final List<FieldDetails> fieldDetailsList = Arrays.asList(
            new FieldDetails<TargetFile, String>("entityId", (TargetFile t) -> t.getEntityId(), (TargetFile t1, String t2) -> t1.setEntityId(t2)),
            new FieldDetails<TargetFile, Date>("createdDate", (TargetFile t) -> t.getCreatedDate(), (TargetFile t1, Date t2) -> t1.setCreatedDate(t2)),
            new FieldDetails<TargetFile, String>("text", (TargetFile t) -> t.getText(), (TargetFile t1, String t2) -> t1.setText(t2)));
}
