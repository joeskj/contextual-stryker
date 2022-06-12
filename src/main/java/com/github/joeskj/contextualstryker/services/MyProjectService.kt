package com.github.joeskj.contextualstryker.services

import com.intellij.openapi.project.Project
import com.github.joeskj.contextualstryker.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
