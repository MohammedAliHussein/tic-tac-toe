<script>
    import { fly } from "svelte/transition";
    import { circOut } from "svelte/easing";
    import Grid from "../components/Grid.svelte";
    import Waiting from "../components/Waiting.svelte";
    import { onMount } from "svelte";

    export let connectionUrl = "";

    let connection = null;
    let showing = false;
    let waiting = true;
    let connectedCount = 0;

    function animationWait() {
        setTimeout(() => {
            showing = true;
        }, 1200);
    }

    function handleConnectionOpen(event) {

    }

    function handleConnectionMessage(event) {
        console.log(event.data);
    }

    animationWait();

    onMount(() => {
        connection = new WebSocket(`ws://localhost:3001/${connectionUrl}`);
        connection.addEventListener("open", handleConnectionOpen);
        connection.addEventListener("message", handleConnectionMessage);
    });
</script>

{#if showing}
    <div class="game-container" in:fly={{duration: 400, easing: circOut, y: 0}}>
        {#if waiting}
            <Waiting connectedCount={connectedCount}/>
        {:else}
            <!-- <Countdown /> -->
        {/if}
        <!-- <TurnIndicator/> -->
        <Grid connection={connection}/>
        <!-- Icon indicator -->
    </div>
{/if}

<style>
    .game-container {
        width: 100%;
        height: 100vh;
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
    }
</style>