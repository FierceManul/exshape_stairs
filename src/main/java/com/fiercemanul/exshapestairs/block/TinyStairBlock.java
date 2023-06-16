package com.fiercemanul.exshapestairs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TinyStairBlock extends Block implements SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Half> HALF = BlockStateProperties.HALF;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final VoxelShape SHAPE_ND = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    protected static final VoxelShape SHAPE_SD = Block.box(0.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_WD = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_ED = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_NU = Block.box(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_SU = Block.box(0.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_WU = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_EU = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_NWD = Block.box(0.0D, 0.0D, 0.0D, 8.0D, 8.0D, 8.0D);
    protected static final VoxelShape SHAPE_NED = Block.box(8.0D, 0.0D, 0.0D, 16.0D, 8.0D, 8.0D);
    protected static final VoxelShape SHAPE_SED = Block.box(8.0D, 0.0D, 8.0D, 16.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_SWD = Block.box(0.0D, 0.0D, 8.0D, 8.0D, 8.0D, 16.0D);
    protected static final VoxelShape SHAPE_NWU = Block.box(0.0D, 8.0D, 0.0D, 8.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_NEU = Block.box(8.0D, 8.0D, 0.0D, 16.0D, 16.0D, 8.0D);
    protected static final VoxelShape SHAPE_SEU = Block.box(8.0D, 8.0D, 8.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SHAPE_SWU = Block.box(0.0D, 8.0D, 8.0D, 8.0D, 16.0D, 16.0D);


    public TinyStairBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, Half.BOTTOM)
                .setValue(SHAPE, StairsShape.STRAIGHT)
                .setValue(WATERLOGGED, Boolean.FALSE));
    }



    @Override
    public BlockState getStateForPlacement(BlockPlaceContext placeContext) {
        Direction direction = placeContext.getClickedFace();
        BlockPos blockpos = placeContext.getClickedPos();
        FluidState fluidstate = placeContext.getLevel().getFluidState(blockpos);
        BlockState blockstate = this.defaultBlockState()
                .setValue(FACING, placeContext.getHorizontalDirection())
                .setValue(HALF, direction != Direction.DOWN && (direction == Direction.UP || !(placeContext.getClickLocation().y - (double)blockpos.getY() > 0.5D)) ? Half.BOTTOM : Half.TOP)
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
        return blockstate.setValue(SHAPE, getStairsShape(blockstate, placeContext.getLevel(), blockpos));
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor accessor, BlockPos pos, BlockPos pos1) {
        if (state.getValue(WATERLOGGED)) {
            accessor.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(accessor));
        }
        return direction.getAxis().isHorizontal() ? state.setValue(SHAPE, getStairsShape(state, accessor, pos)) : super.updateShape(state, direction, state1, accessor, pos, pos1);
    }

    private static StairsShape getStairsShape(BlockState state, BlockGetter getter, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockState blockstate = getter.getBlockState(pos.relative(direction));
        if (isTinyStairs(blockstate) && state.getValue(HALF) == blockstate.getValue(HALF)) {
            Direction direction1 = blockstate.getValue(FACING);
            if (direction1.getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, getter, pos, direction1.getOpposite())) {
                if (direction1 == direction.getCounterClockWise()) {
                    return StairsShape.OUTER_LEFT;
                }

                return StairsShape.OUTER_RIGHT;
            }
        }

        BlockState blockstate1 = getter.getBlockState(pos.relative(direction.getOpposite()));
        if (isTinyStairs(blockstate1) && state.getValue(HALF) == blockstate1.getValue(HALF)) {
            Direction direction2 = blockstate1.getValue(FACING);
            if (direction2.getAxis() != state.getValue(FACING).getAxis() && canTakeShape(state, getter, pos, direction2)) {
                if (direction2 == direction.getCounterClockWise()) {
                    return StairsShape.INNER_LEFT;
                }

                return StairsShape.INNER_RIGHT;
            }
        }

        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockGetter getter, BlockPos pos, Direction direction) {
        BlockState blockstate = getter.getBlockState(pos.relative(direction));
        return !isTinyStairs(blockstate) || blockstate.getValue(FACING) != state.getValue(FACING) || blockstate.getValue(HALF) != state.getValue(HALF);
    }

    //控制是否与其他方块连接
    public static boolean isTinyStairs(BlockState blockState) {
        return blockState.getBlock() instanceof TinyStairBlock;
    }





    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return getTinyStairShape(state);
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state, BlockGetter getter, BlockPos pos) {
        return getTinyStairShape(state);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }
    private static VoxelShape getTinyStairShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case NORTH -> switch (state.getValue(SHAPE)) {
                case STRAIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_ND : SHAPE_NU;
                case INNER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_ND, SHAPE_SWD) : Shapes.or(SHAPE_NU, SHAPE_SWU);
                case INNER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_ND, SHAPE_SED) : Shapes.or(SHAPE_NU, SHAPE_SEU);
                case OUTER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_NWD : SHAPE_NWU;
                case OUTER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_NED : SHAPE_NEU;
            };
            case SOUTH -> switch (state.getValue(SHAPE)) {
                case STRAIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_SD : SHAPE_SU;
                case INNER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_NED, SHAPE_SD) : Shapes.or(SHAPE_NEU, SHAPE_SU);
                case INNER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_NWD, SHAPE_SD) : Shapes.or(SHAPE_NWU, SHAPE_SU);
                case OUTER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_SED : SHAPE_SEU;
                case OUTER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_SWD : SHAPE_SWU;
            };
            case WEST -> switch (state.getValue(SHAPE)) {
                case STRAIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_WD : SHAPE_WU;
                case INNER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_WD, SHAPE_SED) : Shapes.or(SHAPE_WU, SHAPE_SEU);
                case INNER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_ND, SHAPE_SWD) : Shapes.or(SHAPE_NU, SHAPE_SWU);
                case OUTER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_SWD : SHAPE_SWU;
                case OUTER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_NWD : SHAPE_NWU;
            };
            case EAST -> switch (state.getValue(SHAPE)) {
                case STRAIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_ED : SHAPE_EU;
                case INNER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_NWD, SHAPE_ED) : Shapes.or(SHAPE_NWU, SHAPE_EU);
                case INNER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? Shapes.or(SHAPE_ED, SHAPE_SWD) : Shapes.or(SHAPE_EU, SHAPE_SWU);
                case OUTER_LEFT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_NED : SHAPE_NEU;
                case OUTER_RIGHT -> state.getValue(HALF) == Half.BOTTOM ? SHAPE_SED : SHAPE_SEU;
            };
            default -> Shapes.block();
        };
    }






    public BlockState rotate(LevelAccessor accessor, BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        StairsShape stairsshape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT:
                if (direction.getAxis() == Direction.Axis.Z) {
                    switch (stairsshape) {
                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default:
                            return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
                break;
            case FRONT_BACK:
                if (direction.getAxis() == Direction.Axis.X) {
                    switch (stairsshape) {
                        case INNER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT:
                            return state.rotate(Rotation.CLOCKWISE_180);
                    }
                }
        }
        return super.mirror(state, mirror);
    }






    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF, SHAPE, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean isPathfindable(BlockState p_56891_, BlockGetter p_56892_, BlockPos p_56893_, PathComputationType p_56894_) {
        return false;
    }

}
