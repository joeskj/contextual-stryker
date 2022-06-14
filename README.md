# contextual-stryker

![Build](https://github.com/joeskj/contextual-stryker/workflows/Build/badge.svg)

[comment]: <> ([![Version]&#40;https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg&#41;]&#40;https://plugins.jetbrains.com/plugin/PLUGIN_ID&#41;)

[comment]: <> ([![Downloads]&#40;https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg&#41;]&#40;https://plugins.jetbrains.com/plugin/PLUGIN_ID&#41;)

[comment]: <> (## Template ToDo list)

[comment]: <> (- [x] Create a new [IntelliJ Platform Plugin Template][template] project.)

[comment]: <> (- [ ] Get familiar with the [template documentation][template].)

[comment]: <> (- [ ] Verify the [pluginGroup]&#40;/gradle.properties&#41;, [plugin ID]&#40;/src/main/resources/META-INF/plugin.xml&#41; and [sources package]&#40;/src/main/kotlin&#41;.)

[comment]: <> (- [ ] Review the [Legal Agreements]&#40;https://plugins.jetbrains.com/docs/marketplace/legal-agreements.html&#41;.)

[comment]: <> (- [ ] [Publish a plugin manually]&#40;https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html?from=IJPluginTemplate&#41; for the first time.)

[comment]: <> (- [ ] Set the Plugin ID in the above README badges.)

[comment]: <> (- [ ] Set the [Deployment Token]&#40;https://plugins.jetbrains.com/docs/marketplace/plugin-upload.html&#41;.)

[comment]: <> (- [ ] Click the <kbd>Watch</kbd> button on the top of the [IntelliJ Platform Plugin Template][template] to be notified about releases containing new features and fixes.)

<!-- Plugin description -->
Adds Stryker to IntelliJ context menus.

You can run Stryker for a particular file or folder by right-clicking on it
and selecting <kbd>Run Stryker for Selected Files(s)</kbd> from any of the following places:
* The Project tool window
* An Editor tab
* Anywhere within a file

Note: Stryker must already be configured in your project.
<!-- Plugin description end -->

## Installation

[comment]: <> (- Using IDE built-in plugin system:)
  
[comment]: <> (  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "contextual-stryker"</kbd> >)

[comment]: <> (  <kbd>Install Plugin</kbd>)
  
- Manually:

  Download the [latest release](https://github.com/joeskj/contextual-stryker/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
