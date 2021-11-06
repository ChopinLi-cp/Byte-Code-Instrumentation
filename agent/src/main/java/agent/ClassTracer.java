package agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import java.util.*;
import java.io.*;
import java.net.URL;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class ClassTracer extends ClassVisitor {
    private String cn;
    int lineNumber;

   static BufferedReader reader;
   static List<String> listAPI;
    // List<String> methodList = new ArrayList<>(Arrays.asList("java/util/ArrayList/add(Ljava/lang/Object;)Z", "java/util/ArrayList/contains(Ljava/lang/Object;)Z"));
    public ClassTracer(ClassVisitor cv) {
    /*	BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("API.txt"));
			String line = reader.readLine();
			while (line != null) {
				System.out.println(line);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/

        super(Opcodes.ASM9, cv);

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
	this.cn = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    public static void loadFile(){
	    try {
            listAPI = new ArrayList<String>();
            // get the file url, not working in JAR file.
            ClassLoader classloader = ClassTracer.class.getClassLoader();
            // URL resource = ClassTracer.class.getClassLoader().getResource("API.txt");
            InputStream is = classloader.getResourceAsStream("API.txt");
            // System.out.println("URL: " + resource);
            if (is == null) {
                System.out.println("file not found");
            } else {
                // failed if files have whitespaces or special characters
                //return new File(resource.getFile());
                InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);

                reader = new BufferedReader(streamReader);
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);
                    // read next line
                    line = reader.readLine();
                    listAPI.add(line);
                }
                reader.close();
            }
	    } catch (IOException e) {
            e.printStackTrace();
        }

    }

    static {
 	    loadFile();
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

	    final String methodId = this.cn + "." + name;
	    return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, desc, signature, exceptions)) {
    		@Override
    		public void visitLineNumber(int line, Label start) {
        		lineNumber = line;
 	 		super.visitLineNumber(line, start);
    		} 
   

	    @Override
	    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {	
			//System.out.println("Owner = " +name);
		String combined_name = owner +"/" +name+desc;	
		if(listAPI.contains(combined_name)){
				
			super.visitLdcInsn(lineNumber);
			super.visitLdcInsn(name);
			super.visitLdcInsn(cn);
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Proxy", name, "(Ljava/util/List;Ljava/lang/Object;ILjava/lang/String;Ljava/lang/String;)Z", false);
		}
		else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
  	}
	};
  }

}
