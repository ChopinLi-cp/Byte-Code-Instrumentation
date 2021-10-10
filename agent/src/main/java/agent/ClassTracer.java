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
    			if( methodId.equals("other/Calculator.add")) 
                   	{		
				super.visitInsn(Opcodes.ICONST_5);	
				super.visitInsn(Opcodes.ICONST_2);
				super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Utility", "callPrint", "(II)Ljava/lang/String;", false);
		   	}
		}
@Override
public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
	if( methodId.equals("other/Calculator.add")){
        	System.out.println("Owner " +owner);
		if(opcode == Opcodes.INVOKEINTERFACE && owner.equals("java/util/List") && name.equals("add") && desc.equals("(Ljava/lang/Object;)Z")){
			super.visitMethodInsn(Opcodes.INVOKESTATIC, "agent/Utility", "testIntrumentation", "(Ljava/util/List;Ljava/lang/Object;)Ljava/lang/String;", false);
		}
		else {
			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}	
	else  {
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
