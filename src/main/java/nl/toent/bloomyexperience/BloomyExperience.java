package nl.toent.bloomyexperience;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.report.ReporterEnvironment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.levelz.entity.LevelExperienceOrbEntity;

import java.io.Console;

public class BloomyExperience implements ModInitializer {
	public static final String MOD_ID = "bloomyexperience";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		PlayerBlockBreakEvents.AFTER.register((world, player, blockPos, blockState, blockEntity) -> {
			if (!world.isClient && player instanceof ServerPlayerEntity serverPlayer) {
				int experienceAmount = 1;
				Block minedBlock = blockState.getBlock();

				if (isCorrectTool(player, blockState)) {
					spawnExp(world, experienceAmount, player);
				}

			}
		});
	}

	private boolean isCorrectTool(PlayerEntity player, BlockState blockState) {
		Item itemInHand = player.getMainHandStack().getItem();

		if (itemInHand instanceof ToolItem tool) {

			return tool.isSuitableFor(blockState) && MiningLevelManager.getRequiredMiningLevel(blockState) <= tool.getMaterial().getMiningLevel();
		}
		return false; // If the item is not a tool, return false
	}

	private void spawnExp(World world, int experienceAmount, PlayerEntity player) {
//		random maximum
		int randMax = 10;
//		random odds 0.5 -> 50% chance
		float randOdds = 0.25f;
		float randomNumber = (float) (Math.random() * randMax);

		if(randomNumber > randMax*(1-randOdds)){
			LevelExperienceOrbEntity levelExp = new LevelExperienceOrbEntity(world, player.getX(), player.getY(), player.getZ(), experienceAmount);
			ExperienceOrbEntity exp = new ExperienceOrbEntity(world, player.getX(), player.getY(), player.getZ(), 0 );
			world.spawnEntity(levelExp);
			world.spawnEntity(exp);
		}
	}
}