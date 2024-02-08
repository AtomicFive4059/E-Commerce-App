

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()


            repositories {
                jcenter()
                maven (url="https://jitpack.io" )
            }




            repositories {
                maven ( url= "https://jitpack.io" )
            }

        repositories{
            google()
            mavenCentral()
        }
    }
}








rootProject.name = "CreativeCart_App"
include(":app")
