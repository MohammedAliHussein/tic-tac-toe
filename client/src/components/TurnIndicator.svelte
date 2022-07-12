<script>
    import { onDestroy, onMount } from "svelte";
    import { fly } from "svelte/transition";
    import { circOut } from "svelte/easing";

    export let connection = null;
    export let displayName = "";
    
    let countingDown = true;
    let counter = 3;
    let turn = false;
    let opponentName = "";

    function getIn(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: 20,
            easing: circOut
        }
    }

    function getOut(delayIndex) {
        return {
            delay: delayIndex * 100,
            y: -20,
            easing: circOut
        }
    }

    function handleMessage(event) {
        const message = JSON.parse(event.data);

        if(message.type == "turn") {
            if((message.player).toString() == displayName) {
                turn = true;
            } else {
                opponentName = (message.player).toString();
                turn = false;
            }
        }

        console.log(message)
    }

    function startCountdown() {
        let id = setInterval(() => {
            if(counter == 0) {
                countingDown = false;
                clearInterval(id);
            }
            counter--;
        }, 1000);        
    }

    onMount(() => {
        connection.addEventListener("message", handleMessage);
        startCountdown();
    });
 
    onDestroy(() => {
        connection.removeEventListener("message", handleMessage);
    });
</script>

<div class="turn-indicator">
    {#if countingDown}
        <h3 in:fly={getIn(1)} out:fly={getOut(0)}>Game Starting in {counter}</h3>
    {:else}
        {#if turn == true}
            <h3 in:fly={getIn(1)} out:fly={getOut(0)}>It's Your Turn</h3>
        {:else}
            <h3 in:fly={getIn(1)} out:fly={getOut(0)}>{opponentName} is Choosing</h3>
        {/if}
    {/if}
</div>

<style>
    .turn-indicator {
        display: flex;
        justify-content: center;
        align-items: center;
        flex-direction: column;
        text-align: center;
        height: 3em;
    }

    h3 {
        color: white;
        animation: fade 2s infinite ease 0.1s;
    }

    @keyframes fade {
        0% {
            opacity: 1;
        }
        50% {
            opacity: 0.3;
        }
        100% {
            opacity: 1;
        }
    }
</style>