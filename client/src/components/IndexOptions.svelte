<script>
    import IndexButton from "./IndexButton.svelte";
</script>

<div class="options">
    <IndexButton text={"Join a Game"} page={"/join"}/>
    <IndexButton text={"Create a Game"} page={"/create"}/>
</div>

<style>
    .options {
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-direction: column;
    }
</style>
