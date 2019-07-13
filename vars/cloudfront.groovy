def call(Map params) {
  pipeline {
    agent any
    options {
      withAWS(region: params.region, credentials:'aws')
    }
    stages {
      stage('Push to CloudFront') {
        when {
          branch 'master'
        }
        steps {
          s3Upload(file:params.path, bucket:params.bucket, acl:'PublicRead')
          cfInvalidate(distribution:params.distribution, paths:['/*'], waitForCompletion: true)
        }
      }
    }
  }
}
