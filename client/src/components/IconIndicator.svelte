<script>
    import { onMount } from "svelte";

    export let icon = null;
    export let connection = null;

    // function handleMessage(event) {
    //     const message = event.data;
    //     if(icon === null) {
    //         icon = message.icon;
    //     }
    // }

    // onMount(() => {
    //     connection.addEventListener("message", handleMessage);
    // });
</script>

<svelte:head>
    <script src="https://kit.fontawesome.com/3ade200140.js" crossorigin="anonymous"></script>
</svelte:head>

<div class="turn-indicator">
    {#if icon == "X"}
        <h3>You are</h3>
        <i class="fa-solid fa-xmark fa-2x"></i>
    {:else}
        <h3>You are</h3>
        <i class="fa-solid fa-o fa-2x"></i>
    {/if}
</div>

<style>
    .turn-indicator {
        display: flex;
        justify-content: center;
        align-items: center;
        justify-content: space-evenly;
    }

    h3 {
        color: white;
        text-align: center;
        margin-right: 0.4em;
    }

    i {
        color: white;
    }
</style>