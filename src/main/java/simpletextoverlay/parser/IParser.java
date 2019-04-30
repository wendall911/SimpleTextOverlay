package simpletextoverlay.parser;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import simpletextoverlay.Alignment;
import simpletextoverlay.value.Value;

public interface IParser {
    boolean load(InputStream inputStream);

    boolean parse(Map<Alignment, List<List<Value>>> format);
}
