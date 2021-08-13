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
        JAVA8 = '/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home'
        JAVA11 = '/Library/Java/JavaVirtualMachines/temurin-11.jdk/Contents/Home'
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh '''
                    ./gradlew clean \
                    ginicapture:assembleDebug ginicapture:assembleRelease \
                    ginicapture-network:assembleDebug ginicapture-network:assembleRelease \
                    ginicapture-accounting-network:assembleDebug ginicapture-accounting-network:assembleRelease \
                    -Dorg.gradle.java.home=$JAVA11
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:testDebugUnitTest -Dorg.gradle.java.home=$JAVA11'
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:jacocoTestDebugUnitTestReport -Dorg.gradle.java.home=$JAVA11'
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew generateJavadocCoverage -Dorg.gradle.java.home=$JAVA8 -PandroidGradlePluginVersion="4.2.2"'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture/build/reports/javadoc-coverage', reportFiles: 'index.html', reportName: 'Gini Capture Javadoc Coverage Report', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-network/build/reports/javadoc-coverage', reportFiles: 'index.html', reportName: 'Gini Capture Network Javadoc Coverage Report', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-accounting-network/build/reports/javadoc-coverage', reportFiles: 'index.html', reportName: 'Gini Capture Accounting Network Javadoc Coverage Report', reportTitles: ''])
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:lint ginicapture:checkstyle ginicapture:pmd -Dorg.gradle.java.home=$JAVA11'
                sh './gradlew ginicapture-network:lint ginicapture-network:checkstyle ginicapture-network:pmd -Dorg.gradle.java.home=$JAVA11'
                sh './gradlew ginicapture-accounting-network:lint ginicapture-accounting-network:checkstyle ginicapture-accounting-network:pmd -Dorg.gradle.java.home=$JAVA11'
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh './gradlew ginicapture:dokkaHtml ginicapture-network:generateJavadoc ginicapture-accounting-network:generateJavadoc -Dorg.gradle.java.home=$JAVA8 -PandroidGradlePluginVersion="4.2.2"'
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture/build/dokka/ginicapture', reportFiles: 'index.html', reportName: 'Gini Capture KDoc', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-network/build/docs/javadoc', reportFiles: 'index.html', reportName: 'Gini Capture Network Javadoc', reportTitles: ''])
                publishHTML([allowMissing: false, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'ginicapture-accounting-network/build/docs/javadoc', reportFiles: 'index.html', reportName: 'Gini Capture Accounting Network Javadoc', reportTitles: ''])
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
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
                            def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                            return status == 0
                        }
                    }
                }
            }
            steps {
                sh '''
                    ./gradlew screenapiexample::clean screenapiexample::assembleRelease \
                    -PreleaseKeystoreFile=screen_api_example.jks -PreleaseKeystorePassword="$SCREEN_API_EXAMPLE_APP_KEYSTORE_PSW" \
                    -PreleaseKeyAlias=screen_api_example -PreleaseKeyPassword="$SCREEN_API_EXAMPLE_APP_KEY_PSW" \
                    -PclientId=$EXAMPLE_APP_CLIENT_CREDENTIALS_USR -PclientSecret=$EXAMPLE_APP_CLIENT_CREDENTIALS_PSW \
                    -Dorg.gradle.java.home=$JAVA11
                '''
                sh '''
                    ./gradlew componentapiexample::clean componentapiexample::assembleRelease \
                    -PreleaseKeystoreFile=component_api_example.jks -PreleaseKeystorePassword="$COMPONENT_API_EXAMPLE_APP_KEYSTORE_PSW" \
                    -PreleaseKeyAlias=component_api_example -PreleaseKeyPassword="$COMPONENT_API_EXAMPLE_APP_KEY_PSW" \
                    -PclientId=$EXAMPLE_APP_CLIENT_CREDENTIALS_USR -PclientSecret=$EXAMPLE_APP_CLIENT_CREDENTIALS_PSW \
                    -Dorg.gradle.java.home=$JAVA11
                '''
                archiveArtifacts '''
                    screenapiexample/build/outputs/apk/release/screenapiexample-release.apk,\
                    componentapiexample/build/outputs/apk/release/componentapiexample-release.apk,\
                    screenapiexample/build/outputs/mapping/release/mapping.txt,\
                    componentapiexample/build/outputs/mapping/release/mapping.txt \
                    -Dorg.gradle.java.home=$JAVA11
                '''
            }
        }
        stage('Release Documentation') {
            when {
                expression {
                    def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                    return status == 0
                }
                expression {
                    boolean publish = false
                    try {
                        def version = sh(returnStdout: true, script: './gradlew -q printLibraryVersion -Dorg.gradle.java.home=$JAVA11').trim()
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
                    ./gradlew publishReleasePublicationToSnapshotsRepository \
                    -PmavenSnapshotsRepoUrl=https://repo.gini.net/nexus/content/repositories/snapshots \
                    -PrepoUser=$NEXUS_MAVEN_USR \
                    -PrepoPassword=$NEXUS_MAVEN_PSW \
                    -Dorg.gradle.java.home=$JAVA11
                '''
            }
        }
        stage('Release Library') {
            when {
                expression {
                    def status = sh(returnStatus: true, script: 'git describe --exact-match HEAD')
                    return status == 0
                }
                expression {
                    boolean publish = false
                    try {
                        def version = sh(returnStdout: true, script: './gradlew -q printLibraryVersion -Dorg.gradle.java.home=$JAVA11').trim()
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
                    ./gradlew publishReleasePublicationToOpenRepository \
                    -PmavenOpenRepoUrl=https://repo.gini.net/nexus/content/repositories/open \
                    -PrepoUser=$NEXUS_MAVEN_USR \
                    -PrepoPassword=$NEXUS_MAVEN_PSW \
                    -Dorg.gradle.java.home=$JAVA11
                '''
            }
        }
    }
}
