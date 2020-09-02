/**
 *
 *  @author Ossowski Marcin S16425
 *
 */

package zad1;

import java.io.FileInputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Tools {

	Map<String, List<String>> map = new LinkedHashMap<>();

	public static Options createOptionsFromYaml(String fileName) throws Exception{

			
			Yaml yaml = new Yaml();
			FileInputStream fileInputStream = new FileInputStream(fileName);
			
			Map <String, Object> map =  yaml.load(fileInputStream);
			Options options = new Options(String.valueOf(map.get("host")),
											(int)map.get("port"),
											(boolean)map.get("concurMode"),
											(boolean)map.get("showSendRes"),
											(Map<String, List<String>>)(map.get("clientsMap")));
			
		
		
		
		return options;
	}
}
