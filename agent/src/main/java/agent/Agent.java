package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Agent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {

               if ("other/Calculator".equals(s))
	       	{
                    final ClassReader reader = new ClassReader(bytes);
                    final ClassWriter writer = new ClassWriter(reader,  ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
                    ClassTracer visitor = new ClassTracer(writer);
                    reader.accept(visitor, ClassReader.EXPAND_FRAMES);
                    return writer.toByteArray();
                }

                return null;
            }
        });
    }

}

