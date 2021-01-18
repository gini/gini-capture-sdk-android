#!/usr/bin/env groovy
pipeline {
    agent any
    environment {
        NEXUS_MAVEN = credentials('external-nexus-maven-repo-credentials')
        GIT = credentials('github')
        COMPONENT_API_EXAMPLE_APP_KEYSTORE_PSW = credentials('gini-vision-library-android_component-api-example-app-release-keystore-password')
        COMPONENT_API_EXAMPLE_APP_KEY_PSW = credentials('gini-vision-library-android_component-api-example-app-release-key-password')
        SCREEN_API_EXAMPLE_APP_KEYSTORE_PSW = credentials('gini-vision-library-android_screen-api-example-app-release-keystore-password')
        SCREEN_API_EXAMPLE_APP_KEY_PSW = credentials('gini-vision-library-android_screen-api-example-app-release-key-password')
        EXAMPLE_APP_CLIENT_CREDENTIALS = credentials('gini-vision-library-android_gini-api-client-credentials')
        COMPONENT_API_EXAMPLE_APP_HOCKEYAPP_API_TOKEN = credentials('gini-vision-library-android_component-api-example-app-hockeyapp-api-token')
        SCREEN_API_EXAMPLE_APP_HOCKEYAPP_API_TOKEN = credentials('gini-vision-library-android_screen-api-example-app-hockeyapp-api-token')
        JAVA9 = '/Users/mobilecd/java-vm/jdk-9.0.4.jdk/Contents/Home'
    }
    stages {
        stage('Import Pipeline Libraries') {
            steps{
                library 'android-tools'
            }
        }
        stage('Build') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh '''
                    ./gradlew clean \
                    ginicapture:assembleDebug ginicapture:assembleRelease \
                    ginicapture-network:assembleDebug ginicapture-network:assembleRelease \
                    ginicapture-accounting-network:assembleDebug ginicapture-accounting-network:assembleRelease
                '''
            }
        }
        stage('Unit Tests') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:testDebugUnitTest -Dorg.gradle.java.home=$JAVA9'
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'ginicapture/build/outputs/test-results/testDebugUnitTest/*.xml'
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture/build/reports/tests/testDebugUnitTest', reportFiles: 'index.html', reportName: 'Unit Test Results', reportTitles: ''])
                }
            }
        }
        stage('Code Coverage') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:jacocoTestDebugUnitTestReport -Dorg.gradle.java.home=$JAVA9'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture/build/jacoco/jacocoHtml', reportFiles: 'index.html', reportName: 'Code Coverage Report', reportTitles: ''])
            }
        }
        stage('Javadoc Coverage') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh './gradlew generateJavadocCoverage'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture/build/reports/javadoc-coverage', reportFiles: 'index.html', reportName: 'GVL Javadoc Coverage Report', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-network/build/reports/javadoc-coverage', reportFiles: 'index.html', reportName: 'GVL Network Javadoc Coverage Report', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-accounting-network/build/reports/javadoc-coverage', reportFiles: 'index.html', reportName: 'GVL Accounting Network Javadoc Coverage Report', reportTitles: ''])
            }
        }
        stage('Code Analysis') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:lint ginicapture:checkstyle ginicapture:pmd'
                sh './gradlew ginicapture-network:lint ginicapture-network:checkstyle ginicapture-network:pmd'
                sh './gradlew ginicapture-accounting-network:lint ginicapture-accounting-network:checkstyle ginicapture-accounting-network:pmd'
                androidLint canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture/build/reports/lint-results.xml', unHealthy: ''
                androidLint canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture-network/build/reports/lint-results.xml', unHealthy: ''
                androidLint canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture-accounting-network/build/reports/lint-results.xml', unHealthy: ''
                checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture/build/reports/checkstyle/checkstyle.xml', unHealthy: ''
                checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture-network/build/reports/checkstyle/checkstyle.xml', unHealthy: ''
                checkstyle canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture-accounting-network/build/reports/checkstyle/checkstyle.xml', unHealthy: ''
                pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture/build/reports/pmd/pmd.xml', unHealthy: ''
                pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture-network/build/reports/pmd/pmd.xml', unHealthy: ''
                pmd canComputeNew: false, defaultEncoding: '', healthy: '', pattern: 'ginicapture-accounting-network/build/reports/pmd/pmd.xml', unHealthy: ''
            }
        }
        stage('Build Documentation') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                withEnv(["PATH+=/usr/local/bin"]) {
                    sh 'scripts/build-sphinx-doc.sh'
                }
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture/src/doc/build/html', reportFiles: 'index.html', reportName: 'Documentation', reportTitles: ''])
            }
        }
        stage('Generate Javadoc') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:dokkaHtml ginicapture-network:generateJavadoc ginicapture-accounting-network:generateJavadoc'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture/build/dokka/ginicapture', reportFiles: 'index.html', reportName: 'GVL KDoc', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-network/build/docs/javadoc', reportFiles: 'index.html', reportName: 'GVL Network Javadoc', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-accounting-network/build/docs/javadoc', reportFiles: 'index.html', reportName: 'GVL Accounting Network Javadoc', reportTitles: ''])
            }
        }
        stage('Archive Artifacts') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh 'cd ginicapture/build/jacoco && zip -r testCoverage.zip jacocoHtml && cd -'
                sh 'cd ginicapture/build/reports && zip -r javadocCoverage.zip javadoc-coverage && cd -'
                archiveArtifacts 'ginicapture/build/outputs/aar/*.aar,ginicapture/build/jacoco/testCoverage.zip,ginicapture/build/reports/javadocCoverage.zip'
            }
        }
        stage('Build Example Apps') {
            when {
                anyOf {
                    not {
                        branch 'master'
                    }
                    allOf {
                        branch 'master'
                        expression {
                            def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                            return !tag.isEmpty()
                        }
                    }
                }
            }
            steps {
                sh './gradlew screenapiexample::clean screenapiexample::assembleRelease -PreleaseKeystoreFile=screen_api_example.jks -PreleaseKeystorePassword="$SCREEN_API_EXAMPLE_APP_KEYSTORE_PSW" -PreleaseKeyAlias=screen_api_example -PreleaseKeyPassword="$SCREEN_API_EXAMPLE_APP_KEY_PSW" -PclientId=$EXAMPLE_APP_CLIENT_CREDENTIALS_USR -PclientSecret=$EXAMPLE_APP_CLIENT_CREDENTIALS_PSW'
                sh './gradlew componentapiexample::clean componentapiexample::assembleRelease -PreleaseKeystoreFile=component_api_example.jks -PreleaseKeystorePassword="$COMPONENT_API_EXAMPLE_APP_KEYSTORE_PSW" -PreleaseKeyAlias=component_api_example -PreleaseKeyPassword="$COMPONENT_API_EXAMPLE_APP_KEY_PSW" -PclientId=$EXAMPLE_APP_CLIENT_CREDENTIALS_USR -PclientSecret=$EXAMPLE_APP_CLIENT_CREDENTIALS_PSW'
                archiveArtifacts 'screenapiexample/build/outputs/apk/release/screenapiexample-release.apk,componentapiexample/build/outputs/apk/release/componentapiexample-release.apk,screenapiexample/build/outputs/mapping/release/mapping.txt,componentapiexample/build/outputs/mapping/release/mapping.txt'
            }
        }
        stage('Release Documentation') {
            when {
                expression {
                    def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                    return !tag.isEmpty()
                }
                expression {
                    boolean publish = false
                    try {
                        def version = sh(returnStdout: true, script: './gradlew -q printLibraryVersion').trim()
                        def sha = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                        input "Release documentation for ${version} from branch ${env.BRANCH_NAME} commit ${sha}?"
                        publish = true
                    } catch (final ignore) {
                        publish = false
                    }
                    return publish
                }
            }
            steps {
                sh 'scripts/release-javadoc.sh $GIT_USR $GIT_PSW'
                sh 'scripts/release-doc.sh $GIT_USR $GIT_PSW'
            }
        }
        stage('Release Library Snapshot') {
            when {
                branch 'develop'
            }
            steps {
                sh '''
                    ./gradlew ginicapture:uploadArchives \
                    ginicapture-network:uploadArchives \
                    ginicapture-accounting-network:uploadArchives \
                    -PmavenSnapshotsRepoUrl=https://repo.gini.net/nexus/content/repositories/snapshots \
                    -PrepoUser=$NEXUS_MAVEN_USR \
                    -PrepoPassword=$NEXUS_MAVEN_PSW
                '''
            }
        }
        stage('Release Library') {
            when {
                expression {
                    def tag = sh(returnStdout: true, script: 'git tag --contains $(git rev-parse HEAD)').trim()
                    return !tag.isEmpty()
                }
                expression {
                    boolean publish = false
                    try {
                        def version = sh(returnStdout: true, script: './gradlew -q printLibraryVersion').trim()
                        def sha = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                        input "Release ${version} from branch ${env.BRANCH_NAME} commit ${sha}?"
                        publish = true
                    } catch (final ignore) {
                        publish = false
                    }
                    return publish
                }
            }
            steps {
                sh '''
                    ./gradlew ginicapture:uploadArchives \
                    ginicapture-network:uploadArchives \
                    ginicapture-accounting-network:uploadArchives \
                    -PmavenRepoUrl=https://repo.gini.net/nexus/content/repositories/open \
                    -PrepoUser=$NEXUS_MAVEN_USR \
                    -PrepoPassword=$NEXUS_MAVEN_PSW
                '''
            }
        }
    }
}
