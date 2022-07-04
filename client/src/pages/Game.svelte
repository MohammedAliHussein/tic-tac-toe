<script>
    import { fly } from "svelte/transition";
    import { circOut } from "svelte/easing";
    import { onMount } from "svelte";
    import Grid from "../components/Grid.svelte";
    import Waiting from "../components/Waiting.svelte";
    import TurnIndicator from "../components/TurnIndicator.svelte";
    import IconIndicator from "../components/IconIndicator.svelte";

    export let connectionUrl = "";

    let connection = null;
    let showing = false;
    let waiting = true;
    let connected = 0;
    let turn = "";
    let icon = "X";

    function animationWait() {
        setTimeout(() => {
            showing = true;
        }, 1200);
    }

    function handleConnectionOpen(event) {
        console.log(event);
    }

    function handleConnectionMessage(event) {
        whileWaiting(event);
        whilePlaying(event);
    }

    function whileWaiting(event) {
        if(waiting == true) {
            const message = JSON.parse(event.data);
            console.log(message);
            connected = message.connected;
            if(connected == 2) {
                setTimeout(() => {
                    waiting = !(message.waiting);
                }, 500);
            } else {
                waiting = !(message.waiting);
            }
        }
    }

    function whilePlaying(event) {

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
            <Waiting connected={connected}/>
        {:else}
            <TurnIndicator turn={turn}/>
        {/if}
        <Grid connection={connection}/>
        <IconIndicator icon={icon}/>
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