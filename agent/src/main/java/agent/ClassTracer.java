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

public class ClassTracer extends ClassVisitor {
    private String cn;
    int lineNumber;
    //String[] listMethods={"add,  addAll, clear, contains. containsAll, equals, hashCode, isEmpty, iterator, remove, removeAll, retainAll, size, toArray"};
    //String[] mapMethods={"clear, containsKey, containsValue, entrySet, equals, get, hashCode, isEmpty, keySet. put, putAll, remove, size, values"};
    //String[] collectionMethods={"add,  addAll, clear, contains. containsAll, equals, hashCode, isEmpty, iterator, remove, removeAll, retainAll, size, toArray"};
    //String[] setMethods={"add, addAll, clear, contains. containsAll, equals, hashCode, isEmpty, iterator, remove, removeAll, retainAll, size, toArray"};
   List<String> methodList = new ArrayList<>(Arrays.asList("java/util/ArrayList/add(Ljava/lang/Object;)Z", "java/util/ArrayList/contains(Ljava/lang/Object;)Z"));
    public ClassTracer(ClassVisitor cv) {
        super(Opcodes.ASM9, cv);

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
	this.cn = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
	    final String methodId = this.cn + "." + name;
		return new MethodVisitor(Opcodes.ASM9, super.visitMethod(access, name, desc, signature, exceptions)) {		
		  /*@Override
	    	public void visitCode() {
//			System.out.println("Method Id = " +methodId);
    			if( methodId.equals("other/DataRace.Request1") || methodId.equals("other/DataRace.Request2") ) 

                   	{		
//				super.visitInsn(Opcodes.ICONST_5);	
//				super.visitInsn(Opcodes.ICONST_2);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Utility", "callPrint", "()Ljava/lang/String;", false);
		   	}
		}*/
    @Override
    public void visitLineNumber(int line, Label start) {
        lineNumber = line;
 	 super.visitLineNumber(line, start);
    } 
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {	
		//System.out.println("Owner = " +name);
		String combined_name = owner +"/" +name+desc;	
		//System.out.println("Name = " +name);
		//System.out.println("Combined Name = " +combined_name);
		if(methodList.contains(combined_name)){
		//if(combined_name.equals("java/util/ArrayList/add(Ljava/lang/Object;)Z")){
			
			super.visitLdcInsn(lineNumber);
			super.visitLdcInsn(name);
			super.visitLdcInsn(cn);
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Proxy", name, "(Ljava/util/List;Ljava/lang/Object;ILjava/lang/String;Ljava/lang/String;)Z", false);
		}
		/*else if(combined_name.equals("java/util/ArrayList/contains(Ljava/lang/Object;)Z")){
		//	System.out.println("ELSE =====>");
			super.visitLdcInsn(lineNumber);
                        super.visitLdcInsn(cn);
			//System.out.println("Contains = " +opcode);
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Utility", "containsProxy", "(Ljava/util/List;Ljava/lang/Object;ILjava/lang/String;)Z", false);
			}*/

		else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
  	}
	};
  }

}
