# Corso Ingegneria del Software 2023 - Cremona

## Project Setup

In order to set up your project, follow these steps

### Clone and push the template to your repo

On GitHub user settings, scroll down to `Developer settings`, then `Personal access token`.

Create one token to be used in place of password when prompted after git command line operations (remember to store it safely).

Using the git command line client for your OS, type the following commands:

```bash
 # clone the repo on your current folder, naming the remote as 'template'
 git clone https://github.com/dragonbanana/ingsoft-2023 --origin template
 # move to the cloned repo
 cd ingsoft-2023/
 # add your repository as 'origin' (default) remote
 git remote add origin https://github.com/YOUR_USERNAME/YOUR_REPO_NAME
 # push the template project to your github repository, setting
 git push --set-upstream origin master
 # alternatively, if you already have some content in your repo (e.g., a README)
 # and YOU WANT TO OVERWRITE IT, force the push
 git push --force --set-upstream origin master

```

then, you can safely remove the 'template' remote by typing `git remote rm template`.

### Customize your project files

- Open the `pom.xml` file in a text editor and substitute the two occurrences of **xxx** with your **team_name**.
- Substitute the occurence of **xxx** with your **team_name** in `Dockerfile`.
- Open `.github/workflows/docker.yaml` and replace on line 29 `pentabanana/ingsoft-2023:latest` with `{your_dockerhub_repository}/{team_name}:latest`
- Import it in IntelliJ
- Customize the `README.md`
- In order to check that everything worked fine, try to build with Maven:
    - from IntelliJ:
        - right-click on the project
        - select `Maven->Reload Project`
        - wait for the build to complete and make sure you have a build success
        - right-click on class `Main` and select `Main App.main()`
        - right-click on class `CalculatorTest` and select `Run CalculatorTest`

### Commit and push your changes:

```
git commit -am "customize project"
git push origin master
```
