package com.ericsson.oss.services.cm.scriptengine.ejb.service.alias;

import static com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.AliasParser.ALIAS_CREATE_COMMAND_CONTEXT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ericsson.oss.services.cli.alias.model.CliAlias;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasCreateInvalidArgumentsException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasIncorrectNumberOfArgumentsException;
import com.ericsson.oss.services.cm.scriptengine.ejb.service.alias.exceptions.AliasInvalidSyntaxException;
import com.ericsson.oss.services.scriptengine.spi.dtos.Command;

public class AliasParserTest {
    AliasParser objectUnderTest = new AliasParser();

    @Test
    public void canExecuteIsCreateAliasCommandWithEmptyCommandContextReturnFalse() {
        assertFalse(objectUnderTest.isCreateAliasCommand(new Command("", "")));
    }

    @Test
    public void canExecuteIsCreateAliasCommandWithNullCommandContextReturnFalse() {
        assertFalse(objectUnderTest.isCreateAliasCommand(new Command(null, "")));
    }

    @Test
    public void canExecuteIsCreateAliasCommandWithAliasCreateCommandContextReturnTrue() {
        assertTrue(objectUnderTest.isCreateAliasCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "")));
    }

    @Test(expected = AliasIncorrectNumberOfArgumentsException.class)
    public void canExecuteValidateAliasExecutionNumberOfCommandArgumentsWithEmptyCommandThrowsAliasIncorrectNumberOfArgumentsException()
            throws Exception {
        final CliAlias cliAlias = createTestCliAlias("\"cmedit get $1\"", "$1");
        objectUnderTest.validateAliasExecutionNumberOfCommandArguments(new Command("ls", ""), cliAlias);
    }

    @Test(expected = AliasIncorrectNumberOfArgumentsException.class)
    public void canExecuteValidateAliasExecutionNumberOfCommandArgumentsWithTooFewArgumentsThrowsAliasIncorrectNumberOfArgumentsException()
            throws Exception {
        final CliAlias cliAlias = createTestCliAlias("\"cmedit get $1 $2\"", "$1 $2");
        objectUnderTest.validateAliasExecutionNumberOfCommandArguments(new Command("ls", ""), cliAlias);
    }

    @Test(expected = AliasIncorrectNumberOfArgumentsException.class)
    public void canExecuteValidateAliasExecutionNumberOfCommandArgumentsWithTooManyArgumentsThrowsAliasIncorrectNumberOfArgumentsException()
            throws Exception {
        final CliAlias cliAlias = createTestCliAlias("\"cmedit get *\"", "");
        objectUnderTest.validateAliasExecutionNumberOfCommandArguments(new Command("ls", "ENodeBFunction"), cliAlias);
    }

    public void canExecuteValidateAliasExecutionNumberOfCommandArgumentsWithOneArgument() throws Exception {
        final CliAlias cliAlias = createTestCliAlias("\"cmedit get $1\"", "$1");
        objectUnderTest.validateAliasExecutionNumberOfCommandArguments(new Command("ls", "ENodeBFunction"), cliAlias);
    }

    @Test
    public void canExecuteValidateAliasExecutionNumberOfCommandArgumentsWithMultipleArguments() throws Exception {
        final CliAlias cliAlias = createTestCliAlias("\"cmedit get $1 $2\"", "$1 $2");
        objectUnderTest.validateAliasExecutionNumberOfCommandArguments(new Command("ls", "ENodeBFunction ManagedElement"), cliAlias);
    }

    @Test
    public void canExecuteValidateAliasExecutionNumberOfCommandArgumentsWithMultipleArgumentsAndSpaces() throws Exception {
        final CliAlias cliAlias = createTestCliAlias("\"cmedit get $1 $2\"", "$1 $2");
        objectUnderTest.validateAliasExecutionNumberOfCommandArguments(new Command("ls", " " + "ENodeBFunction  ManagedElement "), cliAlias);
    }

    @Test
    public void canExecuteValidateAliasExecutionNumberOfCommandArgumentsWithMultipleArgumentsAndNonWhiteSpaceCharacters() throws Exception {
        final CliAlias cliAlias = createTestCliAlias("\"cmedit get $1 $2\"", "$1 $2");
        objectUnderTest.validateAliasExecutionNumberOfCommandArguments(new Command("ls", "ENodeBFunction.* ManagedElement.zz$£%.okok"), cliAlias);
    }

    @Test
    public void canExecuteGetAliasCommandAfterArgumentSubstitutionWithNoArgument() throws AliasCreateInvalidArgumentsException {
        assertTrue(objectUnderTest.getAliasedCommandAfterArgumentSubstitution(new Command("ls", ""), "cmedit get *").equals("cmedit get *"));
    }

    @Test
    public void canExecuteGetAliasCommandAfterArgumentSubstitutionWithOneArgument() throws AliasCreateInvalidArgumentsException {
        assertTrue(objectUnderTest.getAliasedCommandAfterArgumentSubstitution(new Command("ls", "ENodeBFunction"), "cmedit get $1").equals(
                "cmedit get ENodeBFunction"));
    }

    @Test
    public void canExecuteGetAliasCommandAfterArgumentSubstitutionWithTwoArguments() throws AliasCreateInvalidArgumentsException {
        assertTrue(objectUnderTest.getAliasedCommandAfterArgumentSubstitution(new Command("ls", "ENodeBFunction EUtranCellFDD"), "cmedit get $1 $2")
                .equals("cmedit get ENodeBFunction EUtranCellFDD"));
    }

    @Test
    public void canExecuteGetAliasCommandAfterArgumentSubstitutionWithTwoArgumentsReplacedByOneValue() throws AliasCreateInvalidArgumentsException {
        assertTrue(objectUnderTest.getAliasedCommandAfterArgumentSubstitution(new Command("ls", "ENodeBFunction"), "cmedit get $1 $1").equals(
                "cmedit get ENodeBFunction ENodeBFunction"));
    }

    @Test
    public void canExecuteGetAliasCommandAfterArgumentSubstitutionWithTwoArgumentsReplacedByOneValueAndOneOtherArgumentReplacement()
            throws AliasCreateInvalidArgumentsException {
        assertTrue(objectUnderTest.getAliasedCommandAfterArgumentSubstitution(new Command("ls", "ENodeBFunction ENODEB EUtranCellFDD"),
                "cmedit get $1 $3 $1 $3 $2").equals("cmedit get ENodeBFunction EUtranCellFDD ENodeBFunction EUtranCellFDD ENODEB"));
    }

    @Test
    public void canExecuteParseAliasCreateCommandReturnsCliAliasInstance() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1\" \"cmedit get $1     \"")) instanceof CliAlias);
    }

    @Test(expected = AliasInvalidSyntaxException.class)
    public void canExecuteParseAliasCreateCommandWithInvalidSyntaxThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1 $2 $1\"")) instanceof CliAlias);
    }

    @Test(expected = AliasCreateInvalidArgumentsException.class)
    public void canExecuteParseAliasCreateCommandWithSameArgumentUsedMoreThanOnceThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1 $2 $1\" \"cmedit get $1 $2\"")) instanceof CliAlias);
    }

    @Test(expected = AliasInvalidSyntaxException.class)
    public void canExecuteParseAliasCreateCommandWithInvalidArgumentNameZeroThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $0 $1 $2\" \"cmedit get $0 $1 $2\"")) instanceof CliAlias);
    }

    @Test(expected = AliasInvalidSyntaxException.class)
    public void canExecuteParseAliasCreateCommandWithInvalidArgumentNameNegativeThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $-1 $1 $2\" \"cmedit get $-1 $1 $2\"")) instanceof CliAlias);
    }

    @Test(expected = AliasInvalidSyntaxException.class)
    public void canExecuteParseAliasCreateCommandWithInvalidArgumentNameTenThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1 $2 $10\" \"cmedit get $1 $2 $10\"")) instanceof CliAlias);
    }

    @Test(expected = AliasCreateInvalidArgumentsException.class)
    public void canExecuteParseAliasCreateCommandWithInvalidArgumentSequenceStartingFromOneThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1 $2 $4\" \"cmedit get $1 $2 $4\"")) instanceof CliAlias);
    }

    @Test(expected = AliasCreateInvalidArgumentsException.class)
    public void canExecuteParseAliasCreateCommandWithInvalidArgumentSequenceStartingFromTwoThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $2 $3 $4\" \"cmedit get $2 $3 $4\"")) instanceof CliAlias);
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithValidArgumentsReturnsCliAliasInstance() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1 $2 $3\" \"cmedit get $1 $2 $3\"")) instanceof CliAlias);
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithValidArgumentsOneArgumentUsedMoreThanOnceReturnsCliAliasInstance() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT,
                "\"ls $1 $2 $3 $4\" \"cmedit get $1 $2 $3 $1 $2 $3 $1 $2 $3 $4\"")) instanceof CliAlias);
    }

    @Test(expected = AliasCreateInvalidArgumentsException.class)
    public void canExecuteParseAliasCreateCommandWithTooManyArgumentsThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1 $2 $3\" \"cmedit get $1 $2\"")) instanceof CliAlias);
    }

    @Test(expected = AliasCreateInvalidArgumentsException.class)
    public void canExecuteParseAliasCreateCommandWithTooFewArgumentsThrowsException() throws Exception {
        assertTrue(objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1 $2\" \"cmedit get $1 $2 $3\"")) instanceof CliAlias);
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithOneArgumentReturnsCliAliasInstanceWithCorrectFieldValues() throws Exception {
        final CliAlias cliAlias = objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls $1\" \"cmedit get $1\""));
        validateCliAliasInstanceFieldValues(cliAlias, "ls", "get $1", "cmedit", "$1");
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithNoArgumentReturnsCliAliasInstanceWithCorrectFieldValues() throws Exception {
        final CliAlias cliAlias = objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls\" \"cmedit get *\""));
        validateCliAliasInstanceFieldValues(cliAlias, "ls", "get *", "cmedit", "");
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithNoCommandContextReturnsCliAliasInstanceWithCorrectFieldValues() throws Exception {
        final CliAlias cliAlias = objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls\" \"get *\""));
        validateCliAliasInstanceFieldValues(cliAlias, "ls", "*", "get", "");
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithNoCommandReturnsCliAliasInstanceWithCorrectFieldValues() throws Exception {
        final CliAlias cliAlias = objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls\" \"cmedit\""));
        validateCliAliasInstanceFieldValues(cliAlias, "ls", "", "cmedit", "");
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithNoCommandAndSpacesAfterAliasNameReturnsCliAliasInstanceWithRightFieldValues() throws Exception {
        final CliAlias cliAlias = objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls  \" \"cmedit\""));
        validateCliAliasInstanceFieldValues(cliAlias, "ls", "", "cmedit", "");
    }

    @Test(expected = AliasInvalidSyntaxException.class)
    public void canExecuteParseAliasCreateCommandWithSpaceBeforeAliasNameThrowsException() throws Exception {
        objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"  ls\" \"cmedit get *\""));
    }

    @Test(expected = AliasInvalidSyntaxException.class)
    public void canExecuteParseAliasCreateCommandWithSpaceBeforeAliasedCommandThrowsException() throws Exception {
        objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls\" \"   cmedit get *\""));
    }

    @Test
    public void canExecuteParseAliasCreateCommandWithNoCommandAndSpacesAfterAliasedCommandContextReturnsCliAliasInstanceWithRightFieldValues()
            throws Exception {
        final CliAlias cliAlias = objectUnderTest.parseAliasCreateCommand(new Command(ALIAS_CREATE_COMMAND_CONTEXT, "\"ls\" \"cmedit      \""));
        validateCliAliasInstanceFieldValues(cliAlias, "ls", "", "cmedit", "");
    }

    /*
     * P R I V A T E - M E T H O D S
     */
    private CliAlias createTestCliAlias(final String command, final String arguments) {
        final CliAlias cliAlias = new CliAlias();
        cliAlias.command = command;
        cliAlias.arguments = arguments;
        return cliAlias;
    }

    private void validateCliAliasInstanceFieldValues(final CliAlias cliAlias, final String name, final String command, final String commandContext,
            final String arguments) {
        assertTrue(cliAlias.name.equals(name));
        assertTrue(cliAlias.command.equals(command));
        assertTrue(cliAlias.commandContext.equals(commandContext));
        assertTrue(cliAlias.arguments.equals(arguments));
    }
}
