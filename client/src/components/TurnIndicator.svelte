<script>
    import { onMount } from "svelte";

    export let connection = null;
    export let displayName = "";

    function handleMessage(event) {
        const message = event.data;

        if(message.type == "turn") {
            message.player == displayName ? console.log("your turn") : console.log("not your turn");
        }
    }

    onMount(() => {
        connection.addEventListener("message", handleMessage);
    });
</script>

<style>

</style>