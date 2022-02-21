package frege.gradle.tasks

import org.gradle.api.internal.file.FileResolver
import org.gradle.api.tasks.*
import org.gradle.process.internal.DefaultExecActionFactory
import org.gradle.process.internal.JavaExecAction
import groovy.transform.CompileStatic

@CompileStatic
class FregeRepl extends JavaExec {

    static String DEFAULT_SRC_DIR        = "src/main/frege"     // TODO: should this come from a source set?
    static String DEFAULT_CLASSES_SUBDIR = "classes/main"       // TODO: should this come from a convention?

    @Optional @InputDirectory
    File sourceDir = new File(project.projectDir, DEFAULT_SRC_DIR).exists() ?  new File(project.projectDir, DEFAULT_SRC_DIR) : null

    @Optional @OutputDirectory
    File targetDir = new File(project.buildDir, DEFAULT_CLASSES_SUBDIR)

    @Override
    void exec() {
        if (sourceDir != null && !sourceDir.exists() ) {
            def currentDir = new File('.')
            logger.info "Intended source dir '${sourceDir.absolutePath}' doesn't exist. Using current dir '${currentDir.absolutePath}' ."
            sourceDir = currentDir
        }
        getMainClass().set("frege.repl.FregeRepl")
        workingDir = sourceDir ?: project.projectDir
        standardInput = System.in
        setClasspath(project.files(project.configurations.getByName("runtimeClasspath")) + project.files(targetDir.absolutePath))
        super.exec()
    }

}
