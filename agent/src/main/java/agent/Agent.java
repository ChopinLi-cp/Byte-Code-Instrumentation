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

               if ("other/DataRace".equals(s))
	       	{
                    final ClassReader reader = new ClassReader(bytes);
                    final ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS );
                    ClassTracer visitor = new ClassTracer(writer);
                    reader.accept(visitor, 0);
                    return writer.toByteArray();
                }

                return null;
            }
        });
	printStartStopTimes();	
    }


  /*  Runtime.getRuntime().addShutdownHook(new Thread()
    {
      public void run()
      {
        System.out.println("Shutdown Hook is running !");
      }
    });
    System.out.println("Application Terminating ...");

*/

	private static void printStartStopTimes() {
       		final long start = System.currentTimeMillis();

   		//System.err.println("Start at .............................&&&& ");
        	Thread hook = new Thread() {
            		@Override
            		public void run() {
                		long timePassed = System.currentTimeMillis() - start;
                		System.err.println("Stop at ............................. " );
				System.out.println("gggggggggggg " + Utility.resultInterception);
           		 }
		};
        	Runtime.getRuntime().addShutdownHook(hook);
	}

}

