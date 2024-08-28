package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ericsson.oss.services.cli.alias.model.CliAlias;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasCreateInvalidArgumentsException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasIncorrectNumberOfArgumentsException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasInvalidSyntaxException;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;

public class AliasParser {

    public static final String ALIAS_CREATE_COMMAND_CONTEXT = "alias";
    /*
     * An alias command has two parts. alias part-> "ls $1 $2" and aliased command part-> "cmedit get * $1.$2"
     * Six groups are defined to parse this.
     * group 1: ((\\w+)\\s*([\\s+\\$[1-9]]*)) to parse alias part without quote e.g. ls $1 $2
     * group 2: (\\w+) to parse the alias name part e.g. ls
     * group 3: ([\\s+\\$[1-9]]*) to parse the arguments of the alias part e.g. $1 $2
     * group 4: ((\\w+)\\s*(.*)) to parse the aliased command part without quote e.g. cmedit get * $1.$2
     * group 5: (\\w+) to parse the command context of the aliased command e.g. cmedit
     * group 6: (.*) to parse the remaining part of the aliased command e.g. get * $1.$2
     */
    private static final Pattern ALIAS_CREATE_COMMAND_PATTERN = Pattern.compile("\"((\\w+)\\s*([\\s+\\$[1-9]]*))\"\\s+\"((\\w+)\\s*(.*))\"$");
    private static final Pattern ALIAS_ARGUMENT_MATCH_PATTERN = Pattern.compile("(\\$[1-9])");
    private static final Pattern ALIAS_VALIDATE_ARGUMENT_UNIQUENESS_PATTERN = Pattern.compile("(\\$[1-9])(?=.*?\\1)");
    private static final Pattern ALIAS_VALIDATE_ALIAS_EXECUTION_NON_WHITESPACE_PATTERN = Pattern.compile("(\\S+)");

    public boolean isCreateAliasCommand(final Command command) {
        return ALIAS_CREATE_COMMAND_CONTEXT.equals(command.getCommandContext());
    }

    public String getAliasedCommandAfterArgumentSubstitution(final Command command, String aliasedCommand) {
        if (!aliasedCommand.contains("$")) {
            return aliasedCommand;
        }
        final String[] aliasArgumentValues = command.getCommand().trim().split("\\s+");
        final Matcher matcher = ALIAS_ARGUMENT_MATCH_PATTERN.matcher(aliasedCommand);
        while (matcher.find() && aliasedCommand.contains("$")) {
            final int aliasedArgumentNumber = Integer.parseInt(matcher.group(1).substring(1));
            aliasedCommand = aliasedCommand.replaceAll("\\" + matcher.group(1),
                    Matcher.quoteReplacement(aliasArgumentValues[aliasedArgumentNumber - 1]));
        }
        return aliasedCommand;
    }

    public void validateAliasExecutionNumberOfCommandArguments(final Command aliasExecutionCommand, final CliAlias cliAlias)
            throws AliasIncorrectNumberOfArgumentsException {
        int providedNumberOfArguments = 0;
        final Matcher matcher = ALIAS_VALIDATE_ALIAS_EXECUTION_NON_WHITESPACE_PATTERN.matcher(aliasExecutionCommand.getCommand());
        while (matcher.find()) {
            providedNumberOfArguments++;
        }
        final int expectedNumberOfArguments = getArgumentCountForAliasedCommand(cliAlias.arguments);
        if (providedNumberOfArguments != expectedNumberOfArguments) {
            throw new AliasIncorrectNumberOfArgumentsException();
        }
    }
    
    public int getArgumentCountForAliasedCommand(final String aliasedCommand) {
        //TODO: EEITSIK to improve this in the future by introducing 2 separate instance based classed
        final Matcher matcher = ALIAS_ARGUMENT_MATCH_PATTERN.matcher(aliasedCommand);
        int counter = 0;
        while (matcher.find()) {
            counter++;
        }
        return counter;
    }

    public CliAlias parseAliasCreateCommand(final Command command) throws AliasInvalidSyntaxException, AliasCreateInvalidArgumentsException {
        final Matcher matcher = ALIAS_CREATE_COMMAND_PATTERN.matcher(command.getCommand());
        if (!matcher.find()) {
            throw new AliasInvalidSyntaxException();
        }
        validateAliasCreateCommandArguments(matcher.group(3), matcher.group(6));
        final CliAlias cliAlias = new CliAlias();
        cliAlias.name = matcher.group(2);
        cliAlias.arguments = matcher.group(3);
        cliAlias.commandContext = matcher.group(5);
        cliAlias.command = matcher.group(6);
        cliAlias.userId = "";
        return cliAlias;
    }

    /*
     * P R I V A T E - M E T H O D S
     */
    private void validateAliasCreateCommandArguments(final String argumentString, final String aliasedCommand)
            throws AliasCreateInvalidArgumentsException {
        validateArgumentConsistency(argumentString, aliasedCommand);
        validateArgumentConsistency(aliasedCommand, argumentString);
        validateArgumentUniqueness(argumentString);
        validateArgumentSequence(argumentString);
    }

    private void validateArgumentConsistency(final String source, final String target) throws AliasCreateInvalidArgumentsException {
        final Matcher matcher = ALIAS_ARGUMENT_MATCH_PATTERN.matcher(source);
        while (matcher.find()) {
            if (!target.contains(matcher.group())) {
                throw new AliasCreateInvalidArgumentsException();
            }
        }
    }

    private void validateArgumentSequence(final String argumentString) throws AliasCreateInvalidArgumentsException {
        final Matcher matcher = ALIAS_ARGUMENT_MATCH_PATTERN.matcher(argumentString);
        final int numberOfArguments = getArgumentCountForAliasedCommand(argumentString);
        while (matcher.find()) {
            if (Integer.parseInt(matcher.group(1).substring(1)) > numberOfArguments) {
                throw new AliasCreateInvalidArgumentsException();
            }
        }
    }

    private void validateArgumentUniqueness(final String argumentString) throws AliasCreateInvalidArgumentsException {
        final Matcher matcher = ALIAS_VALIDATE_ARGUMENT_UNIQUENESS_PATTERN.matcher(argumentString);
        if (matcher.find()) {
            throw new AliasCreateInvalidArgumentsException();
        }
    }
}