<script>
    import {fade, fly} from "svelte/transition";
    import {circOut} from "svelte/easing";
    import Grid from "../components/Grid.svelte";

    export let connectionUrl = "";
    let showing = false;

    function animationWait() {
        setTimeout(() => {
            showing = true;
        }, 1200);
    }

    animationWait();
</script>

{#if showing}
    <div class="game-container" in:fly={{duration: 400, easing: circOut, y: 0}}>
        <!-- <Waiting /> -->
        <Grid />
        <!-- <TurnIndicator/> -->
    </div>
{/if}

<style>
    .game-container {
        width: 100%;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>