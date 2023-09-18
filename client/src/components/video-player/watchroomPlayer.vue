<template>
    <div class="player-container">
        <ArtplayerComRoom 
        :vid="this.props.vid"
        :theme="this.props.theme"
        :resources="this.props.resources"
        :part="this.props.part"
        :isAdmin="this.props.isAdmin"
        @getInstance="getInstance"
        @ready="ready"
        @play="play"
        @pause="pause"
        @seek="seek"
        @switchPid="switchPid"
        >
        </ArtplayerComRoom>
    </div>
</template>

<script>
import ArtplayerComRoom from "./ArtplayerComRoom.vue";

export default {
    data() {
        return {
            instance: null,
        };
    },
    components: {
        ArtplayerComRoom,
    },
    props: {
        vid: {
            type: Number,
            required: true,
        },
        theme: {
            type: String,
            required: true,
        },
        resources: {
            type: Array,
            required: true,
        },
        part: {
            type: Number,
            required: true,
        },
        isAdmin: {
            type: Boolean,
            required: true,
        },
    },
    methods: {
        getInstance(art) {
            this.instance = art;
            this.$emit("getInstance", this.instance);
        },
        ready() {
            this.$emit("ready");
        },
        play() {
            this.$emit("play");
        },
        pause() {
            this.$emit("pause");
        },
        seek(seek) {
            this.$emit("seek", seek);
        },
        switchPid(pid) {
            this.$emit("switchPid", pid);
        },

    }
};


</script>

<style lang="less" scoped>
.player-container {
    height: 0;
    width: 100%;
    padding-bottom: 56.25%;
    position: relative;
    margin-bottom: 40px;

    .player {
        width: 100%;
        height: 100%;
        position: absolute;
        background-color: black;
    }
}
</style>