import java.io.*;
import java.nio.charset.StandardCharsets;


public class ScriptPython {
  Process mProcess;

  public void runScript(String image_path,String filtercode){
         Process process;
         try{
               process = Runtime.getRuntime().exec("python3 filters.py "+image_path+" "+filtercode);
               mProcess = process;
               process.waitFor();
         }catch(Exception e) {
            System.out.println("Exception Raised" + e.toString());
            System.out.println(e.printStackTrace());
         }
         InputStream stdout = mProcess.getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(stdout,StandardCharsets.UTF_8));
         String line;
         try{
            while((line = reader.readLine()) != null){
                 System.out.println("stdout: "+ line);
            }
         }catch(IOException e){
               System.out.println("Exception in reading output"+ e.toString());
         }
  }
}