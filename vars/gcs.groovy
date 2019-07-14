def call(Map params) {
  pipeline {
    agent any
    stages {
      stage('Push to GCS') {
        when {
          branch 'master'
        }
        steps {
         step([$class: "ClassicUploadStep",
		credentialsId: "woven-computing-234012",
		bucket:"gs://${params.bucket}",
		pattern: params.pattern,
		pathPrefix: params.pathPrefix,
		showInline: true])
        }
      }
    }
  }
}
