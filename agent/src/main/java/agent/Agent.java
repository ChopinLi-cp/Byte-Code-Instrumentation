package agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.*;

public class Agent {


	public static boolean blackListContains(String s){
	final String[] blackList = new String[]{"java.applet", "java.awt", "java.awt.color", "java.awt.datatransfer", "java.awt.dnd", "java.awt.dnd.peer", "java.awt.event", "java.awt.font", "java.awt.geom", "java.awt.im", "java.awt.im.spi", "java.awt.image", "java.awt.image.renderable", "java.awt.peer", "java.awt.print", "java.beans", "java.beans.beancontext", "java.io", "java.lang", "java.lang.ref", "java.lang.reflect", "java.math", "java.net", "java.rmi", "java.rmi.activation", "java.rmi.dgc", "java.rmi.registry", "java.rmi.server", "java.security", "java.security.acl", "java.security.cert", "java.security.interfaces", "java.security.spec", "java.sql", "java.text", "java.text.resources", "java.util", "java.util.jar", "java.util.zip", "javax.accessibility", "javax.naming", "javax.naming.directory", "javax.naming.event", "javax.naming.ldap", "javax.naming.spi", "javax.rmi", "javax.rmi.CORBA", "javax.sound.midi", "javax.sound.midi.spi", "javax.sound.sampled", "javax.sound.sampled.spi", "javax.swing", "javax.swing.border", "javax.swing.colorchooser", "javax.swing.event", "javax.swing.filechooser", "javax.swing.plaf", "javax.swing.plaf.basic", "javax.swing.plaf.metal", "javax.swing.plaf.multi", "javax.swing.table", "javax.swing.text", "javax.swing.text.html", "javax.swing.text.html.parser", "javax.swing.text.rtf", "javax.swing.tree", "javax.swing.undo", "javax.transaction", "org.omg.CORBA", "org.omg.CORBA.DynAnyPackage", "org.omg.CORBA.ORBPackage", "org.omg.CORBA.TypeCodePackage", "org.omg.CORBA.portable", "org.omg.CORBA_2_3", "org.omg.CORBA_2_3.portable", "org.omg.CosNaming", "org.omg.CosNaming.NamingContextPackage", "org.omg.SendingContext", "org.omg.stub.java.rmi", "sun.applet", "sun.applet.resources", "sun.audio", "sun.awt", "sun.awt.color", "sun.awt.dnd", "sun.awt.font", "sun.awt.geom", "sun.awt.im", "sun.awt.image", "sun.awt.image.codec", "sun.awt.motif", "sun.awt.print", "sun.beans.editors", "sun.beans.infos", "sun.dc.path", "sun.dc.pr", "sun.io", "sun.java2d", "sun.java2d.loops", "sun.java2d.pipe", "sun.jdbc.odbc", "sun.misc", "sun.net", "sun.net.ftp", "sun.net.nntp", "sun.net.smtp", "sun.net.www", "sun.net.www.content.audio", "sun.net.www.content.image", "sun.net.www.content.text", "sun.net.www.http", "sun.net.www.protocol.doc", "sun.net.www.protocol.file", "sun.net.www.protocol.ftp", "sun.net.www.protocol.gopher", "sun.net.www.protocol.http", "sun.net.www.protocol.jar", "sun.net.www.protocol.mailto", "sun.net.www.protocol.netdoc", "sun.net.www.protocol.systemresource", "sun.net.www.protocol.verbatim", "sun.rmi.log", "sun.rmi.registry", "sun.rmi.server", "sun.rmi.transport", "sun.rmi.transport.proxy", "sun.rmi.transport.tcp", "sun.security.acl", "sun.security.action", "sun.security.pkcs", "sun.security.provider", "sun.security.tools", "sun.security.util", "sun.security.x509", "sun.tools.jar", "sun.tools.util", "sunw.io", "sunw.util", "com.sun.corba.se.internal.CosNaming", "com.sun.corba.se.internal.corba", "com.sun.corba.se.internal.core", "com.sun.corba.se.internal.iiop", "com.sun.corba.se.internal.io", "com.sun.corba.se.internal.io.lang", "com.sun.corba.se.internal.io.util", "com.sun.corba.se.internal.javax.rmi", "com.sun.corba.se.internal.javax.rmi.CORBA", "com.sun.corba.se.internal.orbutil", "com.sun.corba.se.internal.util", "com.sun.image.codec.jpeg", "com.sun.java.swing.plaf.motif", "com.sun.java.swing.plaf.windows", "com.sun.jndi.cosnaming", "com.sun.jndi.ldap", "com.sun.jndi.rmi.registry", "com.sun.jndi.toolkit.corba", "com.sun.jndi.toolkit.ctx", "com.sun.jndi.toolkit.dir", "com.sun.jndi.toolkit.url", "com.sun.jndi.url.iiop", "com.sun.jndi.url.iiopname", "com.sun.jndi.url.ldap", "com.sun.jndi.url.rmi", "com.sun.media.sound", "com.sun.naming.internal", "com.sun.org.omg.CORBA", "com.sun.org.omg.CORBA.ValueDefPackage", "com.sun.org.omg.CORBA.portable", "com.sun.org.omg.SendingContext", "com.sun.org.omg.SendingContext.CodeBasePackage", "com.sun.rmi.rmid", "com.sun.rsajca", "com.sun.rsasign", "sun.launcher", "org.apache.maven", "agent.", "org.junit", "sun.nio", "sun.reflect", "jdk.internal", "junit.", "com.sun", "junit.framework", "org.hamcrest" };
	
	for(int i = 0; i < blackList.length; ++i) {
        	String libPackage = blackList[i];
		if(s.startsWith(libPackage)){
		//	System.out.println("GOT MATCHHHHHHHHHHHHHHHHHH TTTTTTTTTTTTTTTTTT");
			return true;
		}
 	}
	return false;
}

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader classLoader, String s, Class<?> aClass, ProtectionDomain protectionDomain, byte[] bytes) throws IllegalClassFormatException {
		    
		s = s.replaceAll("[/]","."); 
	//	System.out.println("SHANTO FROM 2nd  AGENT = " + s );
		   // if (!blackList.contains(s) ){
		if(!blackListContains(s)){
			System.out.println("SHANTO FROM AGENT = " + s );
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

	private static void printStartStopTimes() {
       		final long start = System.currentTimeMillis();
        	Thread hook = new Thread() {
            		@Override
            		public void run() {
                		long timePassed = System.currentTimeMillis() - start;
                		System.err.println("Stop at ............................. " );
				int size = Utility.resultInterception.size();
				System.out.println(" Total # Conflicting items are = " + size);
				System.out.println();
				System.out.println(Utility.resultInterception);

			}
		};
        	Runtime.getRuntime().addShutdownHook(hook);
	}
}

