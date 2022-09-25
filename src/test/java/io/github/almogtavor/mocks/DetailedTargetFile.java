package io.github.almogtavor.mocks;

import io.github.almogtavor.model.FieldDetails;

import javax.annotation.Generated;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Generated("io.github.almogtavor.processor.DetailedPojoAnnotationProcessor")
public class DetailedTargetFile {
    public static final List<FieldDetails> fieldDetailsList = Arrays.asList(
            new FieldDetails<TargetFile, String>("itemId", (TargetFile t) -> t.getItemId(), (TargetFile t1, String t2) -> t1.setItemId(t2)),
            new FieldDetails<TargetFile, String>("parentId;", (TargetFile t) -> t.getParentId(), (TargetFile t1, String t2) -> t1.setParentId(t2)),
            new FieldDetails<TargetFile, Date>("receptionTime", (TargetFile t) -> t.getReceptionTime(), (TargetFile t1, Date t2) -> t1.setReceptionTime(t2)),
            new FieldDetails<TargetFile, String>("text", (TargetFile t) -> t.getText(), (TargetFile t1, String t2) -> t1.setText(t2)),
            new FieldDetails<TargetFile, String>("html", (TargetFile t) -> t.getHtml(), (TargetFile t1, String t2) -> t1.setHtml(t2)));
}
