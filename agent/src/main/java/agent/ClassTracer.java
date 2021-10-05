package agent;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
	    	public void visitInsn(int opcode) {
    			if(opcode==Opcodes.RETURN && methodId.equals("other/Calculator.add")) {
//        			System.out.println("visitInsn*****");

                		super.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
               		        super.visitLdcInsn("It is a Simple Addition By Instrumentation");
                		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,  "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                		super.visitInsn(Opcodes.RETURN);
                		super.visitMaxs(0, 0);
      			}
      			super.visitInsn(opcode);
    		}
	};
  }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
