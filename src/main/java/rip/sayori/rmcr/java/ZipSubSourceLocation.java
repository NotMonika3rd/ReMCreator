package rip.sayori.rmcr.java;

import org.fife.rsta.ac.java.buildpath.SourceLocation;
import org.fife.rsta.ac.java.classreader.ClassFile;
import org.fife.rsta.ac.java.rjc.ast.CompilationUnit;
import org.fife.rsta.ac.java.rjc.lexer.Scanner;
import org.fife.rsta.ac.java.rjc.parser.ASTFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipSubSourceLocation implements SourceLocation {
    private final File archive;
    public final String sub;

    public ZipSubSourceLocation(File archive, String sub) {
        this.archive = archive;
        this.sub = sub;
    }

    public CompilationUnit getCompilationUnit(ClassFile cf) throws IOException {
        CompilationUnit cu = null;

        try (ZipFile zipFile = new ZipFile(this.archive)) {
            String entryName = cf.getClassName(true).replaceAll("\\.", "/");
            entryName = entryName + ".java";
            ZipEntry entry = zipFile.getEntry(sub + entryName);

            if (entry != null) {
                InputStream in = zipFile.getInputStream(entry);
                Scanner s = new Scanner(new InputStreamReader(in));
                cu = (new ASTFactory()).getCompilationUnit(entryName, s);
            }
        }

        return cu;
    }

    @Override
    public String getLocationAsString() {
        return archive.getAbsolutePath();
    }

}
