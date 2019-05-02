package simpletextoverlay.printer;

import java.io.File;
import java.util.List;
import java.util.Map;

import simpletextoverlay.util.Alignment;
import simpletextoverlay.value.Value;

public interface IPrinter {
    boolean print(File file, Map<Alignment, List<List<Value>>> format);
}
