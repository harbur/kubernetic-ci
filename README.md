# Kubernetic Pipeline

Shared Jenkins Pipeline for easy CI/CD.

## Quick Start

To use this pipeline, each project needs two files:

a `Jenkinsfile` with the following content:

```groovy
@Library("kubernetic-pipeline") _
kubernetic{}
```

and a `kubernetic.yaml` file with the following structure:

```yaml
images:
  - name: project-a/demo
    path: Dockerfile
    context: .
    tags:
      - ${GIT_BRANCH}
      - ${GIT_COMMIT_SHORT}
      - ${GIT_BRANCH}-${BUILD_NUMBER}

charts:
  - charts/demo

releases:
  - name: demo
    namespace: demo
    path: charts/demos
```

## Global Setup

The pipeline is configurable by two files:

* **Project config**: The `kubernetic.yaml` inside the repository.
* **Global config**: The `properties.yaml` under the `/pipeline/properties.yaml` path.

The format of the Global configuration is the following:

```yaml
registry:
  url: https://eu.gcr.io/my-sample-project-191923
  credentialsId: gcr:my-sample-project-191923
chartRepo: chartmuseum
repos:
  - name: stable
    url: https://kubernetes-charts.storage.googleapis.com/
  - name: chartmuseum
    url: https://chartmuseum.example.com/
    username: myuser
    password: ****
```

## Project Sections

In the **Projects** config the following sections are available:

> The order of the sections in the configuration file is not taken into account.

> Each section executes the entries in order of appearance in the configuration file.

### Images

In a single repository multiple images can be configured to be compiled:

```yaml
images:
  - name: project-a/demo
    path: Dockerfile
    context: .
    tags:
      - ${GIT_BRANCH}
      - ${GIT_COMMIT_SHORT}
      - ${GIT_BRANCH}-${BUILD_NUMBER}
```

For each Image entry the following is executed:

```sh
docker login ${global.registry.url}
docker build -t ${global.registry.url}/${image.name} -f ${image.path} ${image.context}
```

Then for each tag the built image is tagged and pushed to the remote registry.

```sh
docker tag ${global.registry.url}/${image.name} ${global.registry.url}/${image.name}:${tag}
docker push ${global.registry.url}/${image.name}:${tag}
```

### Charts

In a single repository multiple charts can be configured to be build:

```yaml
charts:
  - charts/demo
```

An initial setup of the Helm client is performed:

```sh
helm init -c
# for each global.repos[]
helm repo add ${repo.name} --username ${repo.username} --password ${repo.password} ${repo.url}
```

Then for each Chart entry the following is executed:

```sh
helm dep build ${chart.name}
helm package ${chart.name}
helm lint ${chart.name}
helm push ${file} ${chartRepo}
```

### Releases

In a single repository multiple releases can be configured to be upgraded:

```yaml
releases:
  - name: demo
    namespace: demo
    path: charts/demos
```

For each Release entry the following is executed:

```sh
helm dep build ${release.path}
helm upgrade -i ${release.name} ${release.path} --namespace ${release.namespace}
```

## Global Sections

In the **Global** config the following sections are available:

### Registry

Registry is used to authenticate to a remote private registry in order to be able to pull & push images.

```yaml
registry:
  url: https://eu.gcr.io/my-sample-project-191923
  credentialsId: gcr:my-sample-project-191923
```

[Docker build step plugin] is used to interact with Docker and registry authentication:

```groovy
docker.withRegistry(registry.url, registry.credentialsId) {...}
```

[Docker build step plugin]: https://wiki.jenkins.io/display/JENKINS/Docker+build+step+plugin

### ChartRepo

ChartRepo is the name of the chart repository that is used to push charts:

```yaml
chartRepo: chartmuseum
```

When pushing Charts to a chart repository (e.g. chartmuseum) the [helm push] plugin is used:

```sh
helm push ${file} ${chartRepo}
```

[helm push]: https://github.com/chartmuseum/helm-push

### Repos

Repos is used to manage the Helm repositories used to download & upload charts.

```yaml
repos:
  - name: stable
    url: https://kubernetes-charts.storage.googleapis.com/
  - name: chartmuseum
    url: https://chartmuseum.example.com/
    username: myuser
    password: ****
```

For each Repo the following is executed to add the repository:

```sh
helm repo add ${repo.name} --username ${repo.username} --password ${repo.password} ${repo.url}
```
