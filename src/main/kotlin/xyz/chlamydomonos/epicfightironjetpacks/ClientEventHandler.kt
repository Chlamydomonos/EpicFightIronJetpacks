package xyz.chlamydomonos.epicfightironjetpacks

import com.blakebr0.ironjetpacks.util.JetpackUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.world.phys.Vec2
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import yesman.epicfight.api.animation.LivingMotions
import yesman.epicfight.api.client.forgeevent.UpdatePlayerMotionEvent

@EventBusSubscriber(modid = EpicFightIronJetpacks.ID, value = [Dist.CLIENT])
object ClientEventHandler {
    @SubscribeEvent
    fun onUpdatePlayerMotion(event: UpdatePlayerMotionEvent) {
        if (event.playerPatch.original !is LocalPlayer) {
            return
        }

        val player = event.playerPatch.original as LocalPlayer
        if (JetpackUtils.isFlying(player)) {
            val moveVec = player.input.moveVector
            val realMoveVec = Vec2(moveVec.x * player.forward.x.toFloat(), moveVec.y * player.forward.z.toFloat())
            val deltaMovement = player.getDeltaMovementLerped(Minecraft.getInstance().partialTick)
            val deltaMovementXZ = Vec2(deltaMovement.x.toFloat(), deltaMovement.z.toFloat())
            val cosAngle = realMoveVec.dot(deltaMovementXZ) / (realMoveVec.length() * deltaMovementXZ.length())
            event.playerPatch.currentLivingMotion = if (moveVec.length() <= 0.2 || cosAngle < -0.5 || deltaMovementXZ.length() <= 0.1) {
                LivingMotions.CREATIVE_IDLE
            } else LivingMotions.CREATIVE_FLY
        }
    }
}