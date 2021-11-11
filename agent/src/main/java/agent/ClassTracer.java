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

public class ClassTracer extends ClassVisitor {
    private String cn;
    int lineNumber;

   static BufferedReader reader;
   static List<String> listAPI;
   public ClassTracer(ClassVisitor cv) {
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
                        reader = new BufferedReader(new FileReader("agent/src/main/java/agent/API.txt"));
                        String line = reader.readLine();
                        while (line != null) {
                                System.out.println(line);
                                // read next line
                                line = reader.readLine();
				listAPI.add(line);
                        }
                        reader.close();
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
		String combined_name = owner +"/" +name+desc;
		if(listAPI.contains(combined_name)){
			String methodParameter = combined_name.substring(combined_name.indexOf("(")+1,combined_name.indexOf(")"));
			String  returnVal = combined_name.substring(combined_name.lastIndexOf(")") + 1);
			super.visitLdcInsn(lineNumber);
			super.visitLdcInsn(name);
			super.visitLdcInsn(cn);
			//System.out.println("Combined Name %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% " +owner +" " + name) ;	
			String proxyMethodSignature = "(L" + owner + ";"  +methodParameter+ "ILjava/lang/String;Ljava/lang/String;)" + returnVal;
			System.out.println("..............." + proxyMethodSignature); 
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Proxy", name, proxyMethodSignature, false);
		}
		else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
  	}
	};
  }

}
