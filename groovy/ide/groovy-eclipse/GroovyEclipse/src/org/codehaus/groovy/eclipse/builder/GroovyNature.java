package org.codehaus.groovy.eclipse.builder;

import org.codehaus.groovy.eclipse.GroovyPlugin;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * @see IProjectNature
 */
public class GroovyNature implements IProjectNature {
	private IProject project;

	/**
	 *  
	 */
	public GroovyNature() {
	}

	/**
	 * @see IProjectNature#configure
	 */
	public void configure() throws CoreException {
		// add a groovy builder
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i)
			if (commands[i]
				.getBuilderName()
				.equals(GroovyPlugin.GROOVY_BUILDER))
				return;

		ICommand command = description.newCommand();
		command.setBuilderName(GroovyPlugin.GROOVY_BUILDER);
		ICommand[] newCommands = new ICommand[commands.length + 1];
		System.arraycopy(commands, 0, newCommands, 0, commands.length);
		newCommands[newCommands.length - 1] = command;
		description.setBuildSpec(newCommands);
		getProject().setDescription(description, null);
	}

	public void deconfigure() throws CoreException {
		IProjectDescription description = getProject().getDescription();
		ICommand[] commands = description.getBuildSpec();
		for (int i = 0; i < commands.length; ++i) {
			if (commands[i]
				.getBuilderName()
				.equals(GroovyPlugin.GROOVY_BUILDER)) {
				ICommand[] newCommands = new ICommand[commands.length - 1];
				System.arraycopy(commands, 0, newCommands, 0, i);
				System.arraycopy(
					commands,
					i + 1,
					newCommands,
					i,
					commands.length - i - 1);
				description.setBuildSpec(newCommands);
				getProject().setDescription(description, null);
				return;
			}
		}
	}

	/**
	 * @see IProjectNature#getProject
	 */
	public IProject getProject() {
		return project;
	}

	/**
	 * @see IProjectNature#setProject
	 */
	public void setProject(IProject project) {
		this.project = project;
	}
}
