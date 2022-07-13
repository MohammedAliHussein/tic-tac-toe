<script>
    import { fly } from "svelte/transition";
    import { circOut } from "svelte/easing";
    import { onDestroy, onMount } from "svelte";

    import Grid from "../components/Grid.svelte";
    import Waiting from "../components/Waiting.svelte";
    import TurnIndicator from "../components/TurnIndicator.svelte";
    import IconIndicator from "../components/IconIndicator.svelte";

    export let connectionUrl = "";
    export let displayName = "";

    let connection = null;
    let showing = false;
    let waiting = true;
    let icon = "";

    function animationWait() {
        setTimeout(() => {
            showing = true;
        }, 1200);
    }

    function handleConnectionOpen(event) {
        console.log(event);
    }

    function handleConnectionMessage(event) {
        const message = JSON.parse(event.data);

        switch(message.type) {
            case "waiting":
                icon = message.icon;
                break;
            case "starting":
                icon = message.icon;
                waiting = false;
                break;
        }
    }

    animationWait();

    onMount(() => {
        connection = new WebSocket(`ws://localhost:3001/${connectionUrl}?name=${displayName}`);
        connection.addEventListener("open", handleConnectionOpen);
        connection.addEventListener("message", handleConnectionMessage);
    });

    onDestroy(() => {
        connection.removeEventListener("open", handleConnectionOpen);        
        connection.removeEventListener("message", handleConnectionMessage);
    });
</script>

{#if showing}
    <div class="game-container" in:fly={{duration: 400, easing: circOut, y: 0}}>
        {#if waiting}
            <Waiting />
        {:else}
            <TurnIndicator connection={connection} displayName={displayName}/>
        {/if}
        <Grid connection={connection} icon={icon} displayName={displayName}/>
        <IconIndicator connection={connection} icon={icon}/>
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