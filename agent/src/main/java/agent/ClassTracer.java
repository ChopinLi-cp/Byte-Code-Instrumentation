package agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.TypePath;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;

public class ClassTracer extends ClassVisitor {
    private String cn;
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
		  @Override
	    	public void visitCode() {
//			System.out.println("Method Id = " +methodId);
    			if( methodId.equals("other/DataRace.Request1") || methodId.equals("other/DataRace.Request2") ) 

                   	{		
//				super.visitInsn(Opcodes.ICONST_5);	
//				super.visitInsn(Opcodes.ICONST_2);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Utility", "callPrint", "()Ljava/lang/String;", false);
		   	}
		}
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {	
		//System.out.println("Owner = " +name);
		if(opcode == Opcodes.INVOKEINTERFACE && owner.equals("java/util/List") && desc.equals("(Ljava/lang/Object;)Z")){
			if(name.equals("add")){
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Utility", "addProxy", "(Ljava/util/List;Ljava/lang/Object;)Ljava/lang/String;", false);
			}
			else if(name.equals("contains")){
				//System.out.println("Contains = " +opcode);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Utility", "containsProxy", "(Ljava/util/List;Ljava/lang/Object;)Ljava/lang/String;", false);
			}
		}

		else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
  }
	};
  }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
