package nl.Steffion.BlockHunt;
/**
 * Steffion's Engine - Made by Steffion.
 *
 * You're allowed to use this engine for own usage, you're not allowed to
 * republish the engine. Using this for your own plugin is allowed when a
 * credit is placed somewhere in the plugin.
 *
 * Thanks for your cooperate!
 *
 * @author Steffion
 */
import nl.Steffion.BlockHunt.Managers.ConfigManager;

public enum ConfigC {


	chat_tag("[" + BlockHunt.pdfFile.getName() + "] ", MemoryStorage.config), chat_normal("&b", MemoryStorage.config), chat_warning("&c", MemoryStorage.config), chat_error("&c", MemoryStorage.config), chat_arg("&e",
			MemoryStorage.config), chat_header("&9", MemoryStorage.config), chat_headerhigh("%H_______.[ %A%header%%H ]._______", MemoryStorage.config),

	commandEnabled_info(true, MemoryStorage.config), commandEnabled_help(true, MemoryStorage.config), commandEnabled_reload(true, MemoryStorage.config), commandEnabled_join(true, MemoryStorage.config), commandEnabled_leave(
			true, MemoryStorage.config), commandEnabled_list(true, MemoryStorage.config), commandEnabled_shop(true, MemoryStorage.config), commandEnabled_start(true, MemoryStorage.config), commandEnabled_wand(true,
			MemoryStorage.config), commandEnabled_create(true, MemoryStorage.config), commandEnabled_set(true, MemoryStorage.config), commandEnabled_setwarp(true, MemoryStorage.config), commandEnabled_remove(true,
			MemoryStorage.config), commandEnabled_tokens(true, MemoryStorage.config),

	autoUpdateCheck(true, MemoryStorage.config), autoDownloadUpdate(false, MemoryStorage.config),

	wandIDname("STICK", MemoryStorage.config), wandName("%A&l" + BlockHunt.pdfFile.getName() + "%N's selection wand", MemoryStorage.config), wandDescription(new String[] {
			"%NUse this item to select an arena for your arena.", "%ALeft-Click%N to select point #1.", "%ARight-Click%N to select point #2.",
			"%NUse the create command to define your arena.", "%A/" + BlockHunt.pdfFile.getName() + " <help|h>" }, MemoryStorage.config),

	shop_title("%H&lBlockHunt %NShop", MemoryStorage.config), shop_price("%NPrice: %A%amount% %Ntokens.", MemoryStorage.config),

	shop_blockChooserv1Enabled(true, MemoryStorage.config), shop_blockChooserv1IDname("BOOK", MemoryStorage.config), shop_blockChooserv1Price(3000, MemoryStorage.config), shop_blockChooserv1Name(
			"%H&lBlockHunt Chooser", MemoryStorage.config), shop_blockChooserv1Description(new String[] { "%NUse this item before the arena starts.",
			"%ARight-Click%N in the lobby and choose", "%Nthe block you want to be!", "&6Unlimited uses." }, MemoryStorage.config),

	shop_BlockHuntPassv2Enabled(true, MemoryStorage.config), shop_BlockHuntPassv2IDName("NAME_TAG", MemoryStorage.config), shop_BlockHuntPassv2Price(150, MemoryStorage.config), shop_BlockHuntPassv2Name(
			"%H&lBlockHunt Pass", MemoryStorage.config), shop_BlockHuntPassv2Description(new String[] { "%NUse this item before the arena starts.",
			"%ARight-Click%N in the lobby and choose", "%Nif you want to be a Hider or a Seeker!", "&61 time use.", }, MemoryStorage.config),

	sign_LEAVE(new String[] { "%H[" + BlockHunt.pdfFile.getName() + "%H]", "&4LEAVE", "&8Right-Click", "&8To leave." }, MemoryStorage.config), sign_SHOP(new String[] {
			"%H[" + BlockHunt.pdfFile.getName() + "%H]", "&4SHOP", "&8Right-Click", "&8To shop." }, MemoryStorage.config), sign_WAITING(new String[] {
			"%H[" + BlockHunt.pdfFile.getName() + "%H]", "%A%arenaname%", "%A%players%%N/%A%maxplayers%", "&8Waiting..." }, MemoryStorage.config), sign_STARTING(new String[] {
			"%H[" + BlockHunt.pdfFile.getName() + "%H]", "%A%arenaname%", "%A%players%%N/%A%maxplayers%", "&2Start: %A%timeleft%" }, MemoryStorage.config), sign_INGAME(new String[] {
			"%H[" + BlockHunt.pdfFile.getName() + "%H]", "%A%arenaname%", "%A%players%%N/%A%maxplayers%", "%EIngame: %A%timeleft%" }, MemoryStorage.config),

	scoreboard_enabled(true, MemoryStorage.config), scoreboard_title("%H[" + BlockHunt.pdfFile.getName() + "]", MemoryStorage.config), scoreboard_timeleft("%ATime left:", MemoryStorage.config), scoreboard_seekers(
			"%NSeekers:", MemoryStorage.config), scoreboard_hiders("%NHiders:", MemoryStorage.config),

	requireInventoryClearOnJoin(false, MemoryStorage.config),

	log_enabledPlugin("%TAG%N%name%&a&k + %N%version% is now Enabled. Made by %A%authors%%N.", MemoryStorage.messages), log_disabledPlugin(
			"%TAG%N%name%&c&k - %N%version% is now Disabled. Made by %A%authors%%N.", MemoryStorage.messages),

	help_info("%NDisplays the plugin's info.", MemoryStorage.messages), help_help("%NShows a list of commands.", MemoryStorage.messages), help_reload("%NReloads all configs.", MemoryStorage.messages), help_join(
			"%NJoins a " + BlockHunt.pdfFile.getName() + " game.", MemoryStorage.messages), help_leave("%NLeave a " + BlockHunt.pdfFile.getName() + " game.", MemoryStorage.messages), help_list(
			"%NShows a list of available arenas.", MemoryStorage.messages), help_shop("%NOpens the " + BlockHunt.pdfFile.getName() + " shop.", MemoryStorage.messages), help_start(
			"%NForces an arena to start.", MemoryStorage.messages), help_wand("%NGives you the wand selection tool.", MemoryStorage.messages), help_create(
			"%NCreates an arena from your selection.", MemoryStorage.messages), help_set("%NOpens a panel to set settings.", MemoryStorage.messages), help_setwarp(
			"%NSets warps for your arena.", MemoryStorage.messages), help_remove("%NDeletes an Arena.", MemoryStorage.messages), help_tokens("%NChange someones tokens.", MemoryStorage.messages),

	button_add("%NAdd %A%1%%N to %A%2%%N", MemoryStorage.messages), button_add2("Add", MemoryStorage.messages), button_setting("%NSetting %A%1%%N is now: %A%2%%N.", MemoryStorage.messages), button_remove(
			"%NRemove %A%1%%N from %A%2%%N", MemoryStorage.messages), button_remove2("Remove", MemoryStorage.messages),

	normal_reloadedConfigs("%TAG&aReloaded all configs!", MemoryStorage.messages), normal_joinJoinedArena("%TAG%A%playername%%N joined your arena. (%A%1%%N/%A%2%%N)", MemoryStorage.messages), normal_leaveYouLeft(
			"%TAG%NYou left the arena! Thanks for playing!", MemoryStorage.messages), normal_leaveLeftArena("%TAG%A%playername%%N left your arena. (%A%1%%N/%A%2%%N)", MemoryStorage.messages), normal_startForced(
			"%TAG%NYou forced to start arena '%A%arenaname%%N'!", MemoryStorage.messages), normal_wandGaveWand("%TAG%NHere you go! &o(Use the %A&o%type%%N&o!)", MemoryStorage.messages), normal_wandSetPosition(
			"%TAG%NSet position %A#%number%%N to location: (%A%x%%N, %A%y%%N, %A%z%%N).", MemoryStorage.messages), normal_createCreatedArena(
			"%TAG%NCreated an arena with the name '%A%name%%N'.", MemoryStorage.messages), normal_lobbyArenaIsStarting("%TAG%NThe arena will start in %A%1%%N second(s)!", MemoryStorage.messages), normal_lobbyArenaStarted(
			"%TAG%NThe arena has been started! The seeker is coming to find you in %A%secs%%N seconds!", MemoryStorage.messages), normal_ingameSeekerChoosen(
			"%TAG%NPlayer %A%seeker%%N has been choosen as seeker!", MemoryStorage.messages), normal_ingameBlock("%TAG%NYou're disguised as a(n) '%A%block%%N' block.", MemoryStorage.messages), normal_ingameArenaEnd(
			"%TAG%NThe arena will end in %A%1%%N second(s)!", MemoryStorage.messages), normal_ingameSeekerSpawned("%TAG%A%playername%%N has spawned as a seeker!", MemoryStorage.messages), normal_ingameGivenSword(
			"%TAG%NYou were given a sword!", MemoryStorage.messages), normal_ingameHiderDied("%TAG%NHider %A%playername%%N was killed by %A%killer%%N!", MemoryStorage.messages), normal_ingameHidersLeft(
			"%NHider(s) left: %A%left%%N", MemoryStorage.messages), normal_ingameSeekerDied("%TAG%NSeeker %A%playername%%N died and will respawn in %A%secs%%N seconds!", MemoryStorage.messages), normal_winSeekers(
			"%TAG%NThe %ASEEKERS%N have won!", MemoryStorage.messages), normal_winHiders("%TAG%NThe %AHIDERS%N have won!", MemoryStorage.messages), normal_setwarpWarpSet(
			"%TAG%NSet warp '%A%warp%%N' to your location!", MemoryStorage.messages), normal_addedToken("%TAG%A%amount%%N tokens were added to your account!", MemoryStorage.messages), normal_removeRemovedArena(
			"%TAG%NRemoved arena '%A%name%%N'!", MemoryStorage.messages), normal_tokensChanged("%TAG%N%option% %A%amount%%N tokens %option2% %A%playername%%N.", MemoryStorage.messages), normal_tokensChangedPerson(
			"%TAG%NPlayer %A%playername%%N %N%option% %A%amount%%N %option2% your tokens.", MemoryStorage.messages), normal_ingameNowSolid(
			"%TAG%NYou're now a solid '%A%block%%N' block!", MemoryStorage.messages), normal_ingameNoMoreSolid("%TAG%NYou're no longer a solid block!", MemoryStorage.messages), normal_shopBoughtItem(
			"%TAG%NYou've bought the '%A%itemname%%N' item!", MemoryStorage.messages), normal_shopChoosenBlock("%TAG%NYou've choosen to be a(n) '%A%block%%N' block!", MemoryStorage.messages), normal_shopChoosenSeeker(
			"%TAG%NYou've choosen to be a %Aseeker%N!", MemoryStorage.messages), normal_shopChoosenHiders("%TAG%NYou've choosen to be a %Ahider%N!", MemoryStorage.messages), normal_ingameBlocksLeft("%TAG%NRemaining blocks: %A%1%%N", MemoryStorage.messages),

	warning_lobbyNeedAtleast("%TAG%WYou need atleast %A%1% player(s) to start the game!", MemoryStorage.messages), warning_ingameNEWSeekerChoosen(
			"%TAG%WThe last seeker left and a new seeker has been choosen!", MemoryStorage.messages), warning_unableToCommand(
			"%TAG%WSorry but that command is disabled in the arena.", MemoryStorage.messages), warning_ingameNoSolidPlace("%TAG%WThat's not a valid place to become solid!",
			MemoryStorage.messages), warning_arenaStopped("%TAG%WThe arena has been forced to stop!", MemoryStorage.messages),

	error_noPermission("%TAG%EYou don't have the permissions to do that!", MemoryStorage.messages), error_notANumber("%TAG%E'%A%1%%E' is not a number!", MemoryStorage.messages), error_commandNotEnabled(
			"%TAG%EThis command has been disabled!", MemoryStorage.messages), error_commandNotFound("%TAG%ECouldn't find the command. Try %A/" + BlockHunt.pdfFile.getName()
			+ " help %Efor more info.", MemoryStorage.messages), error_notEnoughArguments("%TAG%EYou're missing arguments, correct syntax: %A%syntax%", MemoryStorage.messages), error_libsDisguisesNotInstalled(
			"%TAG%EThe plugin '%ALib's Disguises%E' is required to run this plugin! Intall it or it won't work!", MemoryStorage.messages), error_protocolLibNotInstalled(
			"%TAG%EThe plugin '%AProtocolLib%E' is required to run this plugin! Intall it or it won't work!", MemoryStorage.messages), error_noArena(
			"%TAG%ENo arena found with the name '%A%name%%E'.", MemoryStorage.messages), error_onlyIngame("%TAG%EThis is an only in-game command!", MemoryStorage.messages), error_joinAlreadyJoined(
			"%TAG%EYou've already joined an arena!", MemoryStorage.messages), error_joinNoBlocksSet("%TAG%EThere are none blocks set for this arena. Notify the administrator.",
			MemoryStorage.messages), error_joinWarpsNotSet("%TAG%EThere are no warps set for this arena. Notify the administrator.", MemoryStorage.messages), error_joinArenaIngame(
			"%TAG%EThis game has already started.", MemoryStorage.messages), error_joinFull("%TAG%EUnable to join this arena. It's full!", MemoryStorage.messages), error_joinInventoryNotEmpty(
			"%TAG%EYour inventory should be empty before joining!", MemoryStorage.messages), error_leaveNotInArena("%TAG%EYou're not in an arena!", MemoryStorage.messages), error_createSelectionFirst(
			"%TAG%EMake a selection first. Use the wand command: %A/" + BlockHunt.pdfFile.getName() + " <wand|w>%E.", MemoryStorage.messages), error_createNotSameWorld(
			"%TAG%EMake your selection points in the same world!", MemoryStorage.messages), error_setTooHighNumber("%TAG%EThat amount is too high! Max amount is: %A%max%%E.",
			MemoryStorage.messages), error_setTooLowNumber("%TAG%EThat amount is too low! Minimal amount is: %A%min%%E.", MemoryStorage.messages), error_setNotABlock(
			"%TAG%EThat is not a block!", MemoryStorage.messages), error_setwarpWarpNotFound("%TAG%EWarp '%A%warp%%E' is not valid!", MemoryStorage.messages), error_tokensPlayerNotOnline(
			"%TAG%ENo player found with the name '%A%playername%%E'!", MemoryStorage.messages), error_tokensUnknownsetting("%TAG%E'%A%option%%E' is not a known option!", MemoryStorage.messages), error_shopNeedMoreTokens(
			"%TAG%EYou need more tokens before you can buy this item.", MemoryStorage.messages), error_shopMaxSeekersReached(
			"%TAG%ESorry, the maximum amount of seekers has been reached!", MemoryStorage.messages), error_shopMaxHidersReached(
			"%TAG%ESorry, the maximum amount of hiders has been reached!", MemoryStorage.messages),
	error_teleportFailed("%TAG%EUnable to teleport you into the arena.", MemoryStorage.messages),
	error_teleportBlocked("%TAG%EYou cannot teleport while playing!", MemoryStorage.messages);

	public Object value;
	public ConfigManager config;
	public String location;

	/**
	 * Makes an object from the list above.
	 *
	 * @param value
	 *            Setting in the config file.
	 * @param config
	 *            The config file.
	 */
	ConfigC(Object value, ConfigManager config) {
		this.value = value;
		this.config = config;
		this.location = this.name().replaceAll("_", ".");
	}
}
