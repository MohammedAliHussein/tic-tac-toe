<script>
    import IndexButton from "./IndexButton.svelte";
</script>

<div class="options">
    <IndexButton text={"Join a Game"} page={"/join"} delay={2}/>
    <IndexButton text={"Create a Game"} page={"/create"} delay={3}/>
</div>

<style>
    .options {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-direction: column;
        margin-top: 0.5em;
    }
</style>
